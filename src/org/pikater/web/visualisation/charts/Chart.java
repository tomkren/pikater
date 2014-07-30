package org.pikater.web.visualisation.charts;

import java.awt.Color;

import org.pikater.web.visualisation.charts.axis.Axis;
import org.pikater.web.visualisation.charts.axis.CategoricalAxis;
import org.pikater.web.visualisation.charts.coloring.Colorer;
import org.pikater.web.visualisation.renderer.RendererInterface;
import org.pikater.web.visualisation.renderer.RendererInterface.TextAlignment;

public abstract class Chart {
	
	protected Axis horizontalAxis;
	protected Axis verticalAxis;
	protected int offsetx;
	protected int offsety;
	
	
	public Chart(int width,int height,int offsetx, int offsety,RendererInterface renderer,Axis horizontalAxis,Axis verticalAxis){
		setRenderer(renderer);
		this.horizontalAxis=horizontalAxis;
		this.verticalAxis=verticalAxis;
		this.offsetx=offsetx;
		this.offsety=offsety;
		this.chartWidth=width;
		this.chartHeight=height;
	}
	
	protected RendererInterface renderer;
	
	protected void setRenderer(RendererInterface renderer){
		this.renderer=renderer;
	}
	
	/**
	 * It calls renderer's begin() function to initiate rendering
	 * When multiple ChartGenerators use the same renderer only once should be called this function or
	 * the appropriate renderers begin() function
	 */
	public void beginRendering(){
		this.renderer.begin();
	}
	
	public void endRendering(){
		this.renderer.end();
	}
	
	
	protected int xLabelHeight=60;
	protected int chartHeight;
	protected int chartWidth;
	protected int xLeftMargin=10;
	protected int xRightMargin=10;
	protected int yBottomMargin=10;
	protected int yTopMargin=10;
	protected int yLabelWidth=40;
	protected int tickSize=10;
	
	public void setMargin(int margin){
		this.setXLeftMargin(margin);
		this.setXRightMargin(margin);
		this.setYTopMargin(margin);
		this.setYBottomMargin(margin);
	}
	
	public void setLabelSize(int labelSize){
		this.setxLabelHeight(labelSize);
		this.setYLabelWidth(labelSize);
	}
	
	public int getxLabelHeight() {
		return xLabelHeight;
	}

	public void setxLabelHeight(int xLabelHeight) {
		this.xLabelHeight = xLabelHeight;
	}

	public int getAreaHeight() {
		return chartHeight-xLabelHeight;
	}

	public int getXLeftMargin() {
		return xLeftMargin;
	}

	public void setXLeftMargin(int xLeftMargin) {
		this.xLeftMargin = xLeftMargin;
	}

	public int getXRightMargin() {
		return xRightMargin;
	}

	public void setXRightMargin(int xRightMargin) {
		this.xRightMargin = xRightMargin;
	}

	public int getYBottomMargin() {
		return yBottomMargin;
	}

	public void setYBottomMargin(int yBottomMargin) {
		this.yBottomMargin = yBottomMargin;
	}

	public int getYTopMargin() {
		return yTopMargin;
	}

	public void setYTopMargin(int yTopMargin) {
		this.yTopMargin = yTopMargin;
	}

	public int getYLabelWidth() {
		return yLabelWidth;
	}

	public void setYLabelWidth(int yLabelWidth) {
		this.yLabelWidth = yLabelWidth;
	}

	public int getAreaWidth() {
		return chartWidth-yLabelWidth;
	}
	
	public int getVerticalAxisHeight(){
		return getAreaHeight()-yBottomMargin-yTopMargin;
	}
	
	public int getHorizontalAxisWidth(){
		return getAreaWidth()-xLeftMargin-xRightMargin;
	}

	public int getChartHeight() {
		return chartHeight;
	}

	public void setChartHeight(int chartHeight) {
		this.chartHeight = chartHeight;
	}

	public int getChartWidth() {
		return chartWidth;
	}

	public void setChartWidth(int chartWidth) {
		this.chartWidth = chartWidth;
	}

	public int getTickSize() {
		return tickSize;
	}

	public void setTickSize(int tickSize) {
		this.tickSize = tickSize;
	}

	public abstract void startChart();
	public abstract void finishChart();
	
	private Color backGroundColor = Color.GRAY;

	public Color getBackGroundColor() {
		return backGroundColor;
	}

	public void setBackGroundColor(Color backGroundColor) {
		this.backGroundColor = backGroundColor;
	}
	

	boolean tickLabelVisible=true;
	
	public boolean isTickLabelVisible() {
		return tickLabelVisible;
	}
	public void setTickLabelVisible(boolean tickLabelVisible) {
		this.tickLabelVisible = tickLabelVisible;
	}

	protected void renderBackground(){
		//drawing the background
		renderer.drawRectangle(
				offsetx+yLabelWidth,
				offsety,
				getAreaWidth(),
				getAreaHeight(),
				getBackGroundColor(),
				Color.BLACK,
				0);
		//drawing the vertical axis
		renderer.drawLine(
				offsetx + yLabelWidth,
				offsety,
				offsetx + yLabelWidth,
				offsety + getAreaHeight(),
				verticalAxis.getAxisColor(),
				1);
		//and the ticks
		for(int i=0;i<verticalAxis.getTickCount();i++){
			int y = offsety+yTopMargin+getVerticalAxisHeight()-(int)(verticalAxis.getTickPosition(i)*getVerticalAxisHeight());
			renderer.drawLine(
					offsetx + yLabelWidth-tickSize,
					y,
					offsetx + yLabelWidth,
					y, 
					verticalAxis.getAxisColor(), 
					1);

			if(tickLabelVisible){
			if(verticalAxis instanceof CategoricalAxis){
				if(i%2==0){
					renderer.drawText(
							verticalAxis.getTickString(i),
							offsetx + yLabelWidth-tickSize-2,
							y+5,
							TextAlignment.Center,
							verticalAxis.getAxisColor(),
							-90);
				}else{
					renderer.drawText(
							verticalAxis.getTickString(i),
							offsetx + yLabelWidth-tickSize-18,
							y+5,
							TextAlignment.Center,
							verticalAxis.getAxisColor(),
							-90);
				}
			}else{
				renderer.drawText(
						verticalAxis.getTickString(i),
						offsetx + yLabelWidth-tickSize-2,
						y+5,
						TextAlignment.Right,
						verticalAxis.getAxisColor());
			}
			}
		}


		//drawing the horizontal axis
		renderer.drawLine(
				offsetx + yLabelWidth,
				offsety + getAreaHeight(),
				offsetx + getChartWidth(),
				offsety + getAreaHeight(),
				horizontalAxis.getAxisColor(),
				1);
		//and the ticks
		for(int i=0;i<horizontalAxis.getTickCount();i++){
			int x = offsetx+yLabelWidth+xLeftMargin+(int)(horizontalAxis.getTickPosition(i) *getHorizontalAxisWidth());
			renderer.drawLine(
					x,
					offsety+getAreaHeight(),
					x,
					offsety+getAreaHeight()+tickSize, 
					horizontalAxis.getAxisColor(), 
					1);
			if(tickLabelVisible){
			//if we have categories write them in two rows
			if(horizontalAxis instanceof CategoricalAxis){
				if(i%2==0){
					renderer.drawText(
							horizontalAxis.getTickString(i),
							x,
							offsety+getAreaHeight()+tickSize+15,
							TextAlignment.Center,
							horizontalAxis.getAxisColor());
				}else{
					renderer.drawText(
							horizontalAxis.getTickString(i),
							x,
							offsety+getAreaHeight()+tickSize+31,
							TextAlignment.Center,
							horizontalAxis.getAxisColor());
				}
			}else{
			renderer.drawText(
					horizontalAxis.getTickString(i),
					x,
					offsety+getAreaHeight()+tickSize+15,
					TextAlignment.Center,
					horizontalAxis.getAxisColor());
			}
			}
		}



	}
	
	Colorer zcolorer;
	
	public Colorer getZcolorer() {
		return zcolorer;
	}

	public void setZcolorer(Colorer zcolorer) {
		this.zcolorer = zcolorer;
	}

	public void renderPoint(double x,double y,double z,int radius){
		renderPoint(x, y, z, radius, this.zcolorer.getColor(z));
	}
	
	
	public void renderPoint(double x,double y,double z,int radius,Colorer colorer){
		renderPoint(x, y, z, radius, colorer.getColor(z));
	}

	public void renderPoint(double x,double y,double z,int radius,Color color){
		renderer.drawCircle(
				this.offsetx+ yLabelWidth+ (int)(this.xLeftMargin+horizontalAxis.getValuePos(x)*this.getHorizontalAxisWidth()),
				this.offsety+this.yTopMargin+getVerticalAxisHeight()+yBottomMargin+(int)(-yBottomMargin-verticalAxis.getValuePos(y)*this.getVerticalAxisHeight()),
				radius,
				Color.BLACK,
				color,
				1);
	}

	public void setHorizontalAxis(Axis axis){
		this.horizontalAxis=axis;
	}

	public void setVerticalAxis(Axis axis){
		this.verticalAxis=axis;
	}
	
	public Axis getHorizontalAxis(){
		return this.horizontalAxis;
	}

	public Axis getVerticalAxis(){
		return this.verticalAxis;
	}

	
	
}
