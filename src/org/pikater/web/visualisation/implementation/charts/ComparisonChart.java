package org.pikater.web.visualisation.implementation.charts;

import java.awt.Color;

import org.pikater.web.visualisation.implementation.charts.axis.Axis;
import org.pikater.web.visualisation.implementation.charts.coloring.Colorer;
import org.pikater.web.visualisation.implementation.renderer.RendererInterface;

public class ComparisonChart extends SingleChart {

	public ComparisonChart(int width, int height, RendererInterface renderer, Axis horizontalAxis, Axis verticalAxis) {
		super(width, height, 0, 0, renderer, horizontalAxis, verticalAxis);
	}

	public void renderSquarePoint(double x, double y, double z, int pointWidth, Colorer colorer) {
		renderSquarePoint(x, y, z, pointWidth, colorer.getColor(z));
	}

	public void renderSquarePoint(double x, double y, double z, int pointWidth, Color color) {
		int cx = offsetx + yLabelWidth + (int) (this.xLeftMargin + horizontalAxis.getValuePos(x) * this.getHorizontalAxisWidth());
		int cy = offsety + yTopMargin + getVerticalAxisHeight() + yBottomMargin + (int) (-yBottomMargin - verticalAxis.getValuePos(y) * this.getVerticalAxisHeight());
		renderer.drawRectangle(cx - pointWidth / 2, cy - pointWidth / 2, pointWidth, pointWidth, color, Color.BLACK, 1);
	}
}
