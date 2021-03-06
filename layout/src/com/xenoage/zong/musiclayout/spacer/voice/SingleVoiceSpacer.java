package com.xenoage.zong.musiclayout.spacer.voice;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.llist;
import static com.xenoage.utils.iterators.ReverseIterator.reverseIt;
import static com.xenoage.zong.musiclayout.spacer.element.RestSpacer.restSpacer;

import java.util.LinkedList;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.RestNotation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.BorderSpacing;
import com.xenoage.zong.musiclayout.spacing.ChordSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.VoiceSpacing;

/**
 * Computes the {@link VoiceSpacing} for a single {@link Voice} regardless of the spacing of other
 * voices or staves. Lyrics are ignored.
 * 
 * Since this class only handles {@link VoiceElement}s, the barlines
 * at the beginning and the end of the measure, implicit initial clefs,
 * time signatures and so on are ignored.
 * 
 * @author Andreas Wenger
 */
public class SingleVoiceSpacer {
	
	public static final SingleVoiceSpacer singleVoiceSpacer = new SingleVoiceSpacer();
	

	public VoiceSpacing compute(Context context, Notations notations) {
		Voice voice = context.score.getVoice(context.mp);
		float is = context.score.getInterlineSpace(context.mp);
		Fraction measureBeats = context.score.getMeasureBeats(context.mp.measure);
		int staffLinesCount = context.score.getStaff(context.mp).getLinesCount();
		return compute(voice, is, measureBeats, staffLinesCount, notations, context.settings);
	}

	VoiceSpacing compute(Voice voice, float interlineSpace, Fraction measureBeats, 
		int staffLinesCount, Notations notations, LayoutSettings layoutSettings) {
		LinkedList<ElementSpacing> ret = llist();

		//special case: no elements in the measure.
		if (voice.getElements().size() == 0) {
			return new VoiceSpacing(voice, interlineSpace, alist((ElementSpacing)
				new BorderSpacing(Fraction._0, 0), new BorderSpacing(measureBeats,
					layoutSettings.spacings.widthMeasureEmpty)));
		}

		//we compute the spacings in reverse order. this is easier, since grace chords
		//use shared space when possible, but are aligned to the right. so we begin
		//at position 0, and create the spacings in reverse direction (thus we find negative positions).
		//at the end, we shift the voice spacing to the right to be aligned at the left measure border,
		//which is position 0.

		//last symbol offset:
		//real offset where the last element's symbol started
		float lastSymbolOffset = 0; //since we do not know the right border yet, we start at 0

		//front gap offset:
		//offset where the last element (including front gap) started.
		float lastFrontGapOffset = lastSymbolOffset;

		//last full element offset:
		//lastSymbolOffset of the last full (non-grace) element
		float lastFullSymbolOffset = lastSymbolOffset;

		//at last beat
		Fraction curBeat = voice.getFilledBeats();
		ret.addFirst(new BorderSpacing(curBeat, lastFrontGapOffset));

		//iterate through the elements in reverse order
		for (VoiceElement element : reverseIt(voice.getElements())) {
			//get the notation
			Notation notation = notations.get(element);
			if (notation == null)
				throw new IllegalStateException("No notation for element " + element);

			//get the width of the element (front gap, symbol's width, rear gap, lyric's width)
			ElementWidth elementWidth = notation.getWidth();

			//add spacing for voice element
			float symbolOffset;
			boolean grace = !element.getDuration().isGreater0();
			if (!grace) {
				//full element
				//share this rear gap and the front gap of the following
				//element + the space of following grace elements, when possible
				//(but use at least minimal distance)
				symbolOffset = Math.min(lastFrontGapOffset - layoutSettings.spacings.widthDistanceMin,
					lastFullSymbolOffset - elementWidth.rearGap) - elementWidth.symbolWidth;
				lastFullSymbolOffset = symbolOffset;
				//update beat cursor
				curBeat = curBeat.sub(element.getDuration());
			}
			else {
				//grace element
				//share this rear gap and the front gap of the following element, when possible
				symbolOffset = Math.min(lastFrontGapOffset, lastSymbolOffset - elementWidth.rearGap) -
					elementWidth.symbolWidth;
			}
			ElementSpacing elementSpacing = null;
			if (notation instanceof RestNotation) {
				//rest spacing
				elementSpacing = restSpacer.compute((RestNotation) notation, curBeat,
					symbolOffset, staffLinesCount);
			}
			else {
				//chord spacing
				elementSpacing = new ChordSpacing((ChordNotation) notation, curBeat, symbolOffset);
			}
			ret.addFirst(elementSpacing);
			lastFrontGapOffset = symbolOffset - elementWidth.frontGap;
			lastSymbolOffset = symbolOffset;
		}

		//shift spacings to the right
		float shift = (-lastFrontGapOffset) + layoutSettings.offsetMeasureStart;
		for (ElementSpacing e : ret)
			e.xIs += shift;

		return new VoiceSpacing(voice, interlineSpace, ilist(ret));
	}

}
