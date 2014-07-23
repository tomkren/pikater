package org.pikater.shared.visualisation.charts;

import java.awt.Color;

import org.pikater.shared.visualisation.charts.axis.Axis;
import org.pikater.shared.visualisation.renderer.RendererInterface;
import org.pikater.shared.visualisation.renderer.RendererInterface.TextAlignment;

public class SingleChart extends Chart{

	private Color horizontalCaptionColor;
	private Color verticalCaptionColor;
	private int captionSize=20;
	
	public SingleChart(){
		super(1000,1000,0,0,null,null,null);
	}
	
	public SingleChart(int width,int height,int offsetx, int offsety,RendererInterface renderer,Axis horizontalAxis,Axis verticalAxis){
		super(width, height, offsetx, offsety, renderer, horizontalAxis, verticalAxis);
	}

	public SingleChart(int width,int height,RendererInterface renderer,Axis horizontalAxis, Axis verticalAxis){
		this(width,height,0,0,renderer,horizontalAxis,verticalAxis);
	}

	private void renderHorizontalCaption(String caption){
		renderer.drawText(
				caption,
				offsetx+yLabelWidth+getAreaWidth()/2,
				offsety+chartHeight-xLabelHeight/3,
				TextAlignment.Center,
				horizontalCaptionColor,
				0,
				captionSize);
	}
	
	private void renderVerticalCaption(String caption){
		renderer.drawText(
				caption,
				offsetx+yLabelWidth/2,
				offsety+getAreaHeight()/2,
				TextAlignment.Center,
				verticalCaptionColor,
				-90,
				captionSize);
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
		if(horizontalAxis.isCaptionVisible()){
			renderHorizontalCaption(horizontalAxis.getCaption());
		}
		if(verticalAxis.isCaptionVisible()){
			renderVerticalCaption(verticalAxis.getCaption());
		}
	}



}
