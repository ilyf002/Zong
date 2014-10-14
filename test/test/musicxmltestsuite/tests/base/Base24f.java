package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static musicxmltestsuite.Utils.ch;
import static musicxmltestsuite.Utils.gr;
import static musicxmltestsuite.Utils.slur;

import com.xenoage.zong.commands.core.music.slur.SlurAdd;
import com.xenoage.zong.core.music.chord.Chord;

/**
 * A grace note with a slur to the actual note. This can be interpreted
 * as acciaccatura or appoggiatura, depending on the existence of a slash. 
 * 
 * @author Andreas Wenger
 */
public interface Base24f
	extends Base {

	@Override default String getFileName() {
		return "24f-GraceNote-Slur.xml";
	}
	
	Chord[] expectedChords = getExpectedChords();
	
	static Chord[] getExpectedChords() {
		Chord[] ret = {
			ch(fr(2, 4), pi('E', 0, 5)),
			gr(fr(1, 16), false, pi('G', 0, 5)),
			ch(fr(2, 4), pi('E', 0, 5))};
		new SlurAdd(slur(ret[1], ret[2])).execute();
		return ret;
	}

}
