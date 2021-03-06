package com.xenoage.zong.core.music.barline;


/**
 * Repeat style of a barline, inspired by MusicXML.
 * 
 * @author Andreas Wenger
 */
public enum BarlineRepeat {
	/** Repeat sign at the right side of a repeated section. */
	Backward,
	/** Repeat sign at the left side of a repeated section. */
	Forward,
	/** Repeat sign at both sides. Only allowed for barlines in the middle of a measure. */
	Both,
	/** No repeat. */
	None;

	/**
	 * True for backward and both.
	 */
	public boolean isBackward() {
		return this == Backward || this == Both;
	}

	/**
	 * True for forward and both.
	 */
	public boolean isForward() {
		return this == Forward || this == Both;
	}

}
