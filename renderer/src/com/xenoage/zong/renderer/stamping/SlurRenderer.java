package com.xenoage.zong.renderer.stamping;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.musiclayout.stampings.SlurStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.bitmap.BitmapStaff;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.slur.SimpleSlurShape;

/**
 * Renderer for a {@link SlurStamping}.
 *
 * @author Andreas Wenger
 */
public class SlurRenderer
	extends StampingRenderer {

	/**
	 * Draws the given {@link SlurStamping} on the given {@link Canvas},
	 * using the given {@link RendererArgs}.
	 */
	@Override public void draw(Stamping stamping, Canvas canvas, RendererArgs args) {
		SlurStamping slur = (SlurStamping) stamping;

		float scaling = args.targetScaling;

		//compute absolute coordinates in px
		float p1xMm = slur.p1.xMm;
		float p2xMm = slur.p2.xMm;
		float c1xMm = p1xMm + slur.c1.xMm;
		float c2xMm = p2xMm + slur.c2.xMm;
		float p1yMm = 0, p2yMm = 0, c1yMm = 0, c2yMm = 0;
		if (canvas.getFormat() == CanvasFormat.Raster) {
			float staff1YMm = slur.staff1.positionMm.y;
			float staff2YMm = slur.staff2.positionMm.y;
			BitmapStaff screenStaff1 = slur.staff1.getBitmapInfo().getBitmapStaff(scaling);
			BitmapStaff screenStaff2 = slur.staff2.getBitmapInfo().getBitmapStaff(scaling);
			float bottomLineMm1 = staff1YMm + screenStaff1.lp0Mm;
			float bottomLineMm2 = staff2YMm + screenStaff2.lp0Mm;
			float isMm1 = screenStaff1.interlineSpaceMm;
			float isMm2 = screenStaff2.interlineSpaceMm;
			p1yMm = bottomLineMm1 - isMm1 * slur.p1.lp / 2;
			p2yMm = bottomLineMm2 - isMm2 * slur.p2.lp / 2;
			c1yMm = p1yMm - isMm1 * slur.c1.lp / 2;
			c2yMm = p2yMm - isMm2 * slur.c2.lp / 2;
		}
		else if (canvas.getFormat() == CanvasFormat.Vector) {
			p1yMm = slur.staff1.computeYMm(slur.p1.lp);
			p2yMm = slur.staff2.computeYMm(slur.p2.lp);
			c1yMm = slur.staff1.computeYMm(slur.p1.lp + slur.c1.lp);
			c2yMm = slur.staff2.computeYMm(slur.p2.lp + slur.c2.lp);
		}

		Point2f p1 = new Point2f(p1xMm, p1yMm);
		Point2f p2 = new Point2f(p2xMm, p2yMm);
		Point2f c1 = new Point2f(c1xMm, c1yMm);
		Point2f c2 = new Point2f(c2xMm, c2yMm);

		Color color = Color.black;

		/* //TEST
		Point2i lastPoint = new Point2i(MathTools.bezier(p1, p2, c1, c2, 0));
		for (int i = 1; i <= iterations; i++)
		{
			float t = 1f * i / iterations;
			float width = 0.7f + (0.5f - Math.abs(t - 0.5f)) * 2.5f;
			Point2i p = new Point2i(MathTools.bezier(p1, p2, c1, c2, t));
		  params.renderTarget.drawLine(lastPoint, p, color, MathTools.clampMin((int) (scaling * width), 1));
		  lastPoint = p;
		} */

		SimpleSlurShape slurShape = new SimpleSlurShape(p1, p2, c1, c2, slur.staff1.is);
		canvas.fillPath(slurShape.getPath(), color);
	}

}
