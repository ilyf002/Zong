package com.xenoage.zong.musiclayout.settings;

import static com.xenoage.utils.base.collections.CollectionUtils.getMax;
import static com.xenoage.utils.base.collections.CollectionUtils.getMin;
import static com.xenoage.utils.base.collections.CollectionUtils.map;
import static com.xenoage.utils.math.Fraction._0;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.xenoage.utils.base.NullUtils;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.xml.XMLReader;

/**
 * Settings for space (in IS) chords need.
 * 
 * @author Andreas Wenger
 */
public class ChordSpacings
{
	
	//duration-to-width mapping
	private final Map<Fraction, Float> durationWidths;
	//cache
	private Fraction durationWidthsLowestDuration = _0, durationWidthsHighestDuration = _0;
	private Map<Fraction, Float> durationWidthsCache = new HashMap<Fraction, Float>();

	
	public static ChordSpacings fromXML(Element e)
		throws IOException
	{
		HashMap<Fraction, Float> durationWidths = map();
		
		//load the duration-to-width mapping
		List<Element> listChords = XMLReader.elements(e, "chord");
		for (Element el : listChords)
		{
			//duration format: x/y, e.g. "1/4"
			Fraction duration = Fraction.fromString(XMLReader.attributeNotNull(el, "duration"));
			//width format: x+y/z, eg. "3+1/2"
			float width = Fraction.fromString(XMLReader.attributeNotNull(el, "width")).toFloat();
			durationWidths.put(duration, width);
		}
		
		return new ChordSpacings(durationWidths);
	}
	

	public ChordSpacings(Map<Fraction, Float> durationWidths)
	{
		this.durationWidths = durationWidths;
		
		//init cache
		//find lowest and highest duration
		durationWidthsLowestDuration = getMin(durationWidths.keySet());
		durationWidthsHighestDuration = getMax(durationWidths.keySet());
	}


	/**
	 * Computes and returns the width that fits to the given duration.
	 */
	public float getWidth(Fraction duration)
	{
		NullUtils.throwNullArg(duration);
		//if available, use defined width
		Float width = durationWidths.get(duration);
		if (width != null)
			return width;
		//if available, use cached computed width
		width = durationWidthsCache.get(duration);
		if (width != null)
			return width;
		//not found. find the greatest lesser duration and the lowest
		//greater duration and interpolate linearly. remember the result
		//to avoid this computation for the future.
		Fraction lowerDur = durationWidthsLowestDuration;
		Fraction higherDur = durationWidthsHighestDuration;
		for (Fraction d : durationWidths.keySet())
		{
			if (d.compareTo(duration) <= 0 && d.compareTo(lowerDur) > 0)
			{
				lowerDur = d;
			}
			if (d.compareTo(duration) >= 0 && d.compareTo(higherDur) < 0)
			{
				higherDur = d;
			}
		}
		float lowerWidth = durationWidths.get(lowerDur);
		float higherWidth = durationWidths.get(higherDur);
		float durationWidth = (lowerWidth + higherWidth) * duration.toFloat() /
			(lowerDur.toFloat() + higherDur.toFloat());
		durationWidthsCache.put(duration, durationWidth);
		return durationWidth;
	}

}