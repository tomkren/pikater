package org.pikater.web.visualisation.implementation.charts;

import java.awt.Color;

import org.pikater.web.visualisation.implementation.charts.axis.Axis;
import org.pikater.web.visualisation.implementation.renderer.RendererInterface;
import org.pikater.web.visualisation.implementation.renderer.RendererInterface.TextAlignment;

public class SingleChart extends Chart {

	private Color horizontalCaptionColor;
	private Color verticalCaptionColor;
	private int captionSize = 20;
	private boolean capitalCaption = false;

	public SingleChart() {
		super(1000, 1000, 0, 0, null, null, null);
	}

	public SingleChart(int width, int height, int offsetx, int offsety, RendererInterface renderer, Axis horizontalAxis, Axis verticalAxis) {
		super(width, height, offsetx, offsety, renderer, horizontalAxis, verticalAxis);
		initDefaults();
	}

	public SingleChart(int width, int height, RendererInterface renderer, Axis horizontalAxis, Axis verticalAxis) {
		this(width, height, 0, 0, renderer, horizontalAxis, verticalAxis);
		initDefaults();
	}

	private void initDefaults() {
		this.setMargin(50);
		this.setLabelSize(150);
		this.setBackGroundColor(Color.getHSBColor(0.0f, 0.0f, 0.95f));
		this.setHorizontalCaptionColor(Color.GRAY);
		this.setVerticalCaptionColor(Color.GRAY);
		this.setCaptionSize(40);
		this.setCapitalCaption(true);
	}

	public boolean isCapitalCaption() {
		return capitalCaption;
	}

	public void setCapitalCaption(boolean capitalCaption) {
		this.capitalCaption = capitalCaption;
	}

	private void renderHorizontalCaption(String caption) {
		renderer.drawText(isCapitalCaption() ? caption.toUpperCase() : caption, offsetx + yLabelWidth + getAreaWidth() / 2, offsety + chartHeight - xLabelHeight / 3, TextAlignment.Center,
				horizontalCaptionColor, 0, captionSize);
	}

	private void renderVerticalCaption(String caption) {
		renderer.drawText(isCapitalCaption() ? caption.toUpperCase() : caption, offsetx + yLabelWidth / 2, offsety + getAreaHeight() / 2, TextAlignment.Center, verticalCaptionColor, -90, captionSize);
	}

	public Color getHorizontalCaptionColor() {
		return horizontalCaptionColor;
	}

	public void setHorizontalCaptionColor(Color horizontalCaptionColor) {
		this.horizontalCaptionColor = horizontalCaptionColor;
	}

	public Color getVerticalCaptionColor() {
		return verticalCaptionColor;
	}

	public void setVerticalCaptionColor(Color verticalCaptionColor) {
		this.verticalCaptionColor = verticalCaptionColor;
	}

	public int getCaptionSize() {
		return captionSize;
	}

	public void setCaptionSize(int captionSize) {
		this.captionSize = captionSize;
	}

	@Override
	public void finishChart() {
		endRendering();
	}

	@Override
	public void startChart() {
		renderBackground();
		if (horizontalAxis.isCaptionVisible()) {
			renderHorizontalCaption(horizontalAxis.getCaption());
		}
		if (verticalAxis.isCaptionVisible()) {
			renderVerticalCaption(verticalAxis.getCaption());
		}
	}

}
