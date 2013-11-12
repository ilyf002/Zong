package com.xenoage.zong.io.symbols;

import org.junit.Test;



/**
 * Test cases for a {@link SVGPathReader}.
 *
 * @author Andreas Wenger
 */
public class SVGPathReaderTest
{

  @Test public void readTest()
  {
  	SVGPathReader p = new AWTSVGPathReader();
    String validPath = "M 100 100 L 300 100 L 200 300 z";
    p.read(validPath);
    validPath = "M200,300 L400,50 L600,300 L800,550 L1000,300";
    p.read(validPath);
  }
  
}