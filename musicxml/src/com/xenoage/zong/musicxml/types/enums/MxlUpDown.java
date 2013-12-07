package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.MaybeNull;

/**
 * MusicXML up-down.
 * 
 * @author Andreas Wenger
 */
public enum MxlUpDown {

	Up,
	Down;

	@MaybeNull public static MxlUpDown read(String s) {
		return Utils.readOrNull("up-down", s, values());
	}

	public String write() {
		return toString().toLowerCase();
	}

}
