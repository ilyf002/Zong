package com.xenoage.zong.io.midi.out;

import com.xenoage.zong.core.header.ColumnHeader;

/**
 * This class calculates the tempo changes for the {@link MidiConverter}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class MidiTempoConverter {

	/**
	 * Writes tempo changes into the given tempo track.
	 * Only tempos found in the {@link ColumnHeader}s are used.
	 * /
	public static void writeTempoTrack(Score score, Repetitions repetitions, int resolution,
																		 MidiSequenceWriter<?> writer, int track) {
		long measurestarttick = 0;
		for (PlayRange playRange : repetitions.getRepetitions()) {
			for (int iMeasure : range(playRange.start.measure, playRange.end.measure)) {
				Fraction start = (playRange.start.measure == iMeasure ? playRange.start.beat : _0);
				Fraction end = (playRange.end.measure == iMeasure ?
					playRange.end.beat : score.getMeasureBeats(iMeasure));

				BeatEList<Tempo> tempos = score.getHeader().getColumnHeader(iMeasure).getTempos();
				if (tempos != null) {
					for (BeatE<Tempo> beatE : tempos) {
						long tick = measurestarttick +
							calculateTickFromFraction(beatE.beat.sub(start), resolution);
						writer.writeTempoChange(track, tick, beatE.getElement().getBeatsPerMinute());
					}
				}

				Fraction measureDuration = end.sub(start);
				measurestarttick += MidiConverter.calculateTickFromFraction(measureDuration, resolution);
			}
		}
	} //*/

}
