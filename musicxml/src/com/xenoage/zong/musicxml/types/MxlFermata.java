package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.types.enums.MxlUprightInverted;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML fermata.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "fermata-shape")
@AllArgsConstructor @Getter @Setter
public final class MxlFermata
	implements MxlNotationsContent {

	public static final String elemName = "fermata";

	@MaybeNull private MxlUprightInverted type;
	@MaybeNull private MxlPrintStyle printStyle;


	@Override public MxlNotationsContentType getNotationsContentType() {
		return MxlNotationsContentType.Fermata;
	}

	/**
	 * Reads the given element and returns it, or returns null if
	 * the element is not supported.
	 */
	@MaybeNull public static MxlFermata read(XmlReader reader) {
		MxlUprightInverted type = MxlUprightInverted.read(reader, "type");
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		return new MxlFermata(type, printStyle);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (type != null)
			type.write(writer, "type");
		if (printStyle != null)
			printStyle.write(writer);
		writer.writeElementEnd();
	}

}