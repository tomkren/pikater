package org.pikater.shared.visualisation.charts;

import java.awt.Color;

import org.pikater.shared.visualisation.datasource.multiple.MultipleArffDataset;
import org.pikater.shared.visualisation.renderer.RendererInterface;
import org.pikater.shared.visualisation.renderer.RendererInterface.TextAlignment;

public class MatrixChart {
	
	SingleChart[][] subcharts;
	
	int count=0;
	
	int singleWidth=100;
	int singleHeight=100;
	
	
	MultipleArffDataset dataset;
	
	int width;
	int height;
	int colorIndex;
	
	int yLabelWidth=50;
	int xLabelHeight=50;
	
	RendererInterface renderer;
	
	
	public MatrixChart(MultipleArffDataset dataset, int width,int height,int count,RendererInterface renderer,int colorIndex){
		this.dataset=dataset;
		this.renderer=renderer;
		
		this.colorIndex=colorIndex;
		
		this.width=width;
		this.height=height;
		
		this.count=count;
		this.subcharts=new SingleChart[count][count];
		
		
		init();
	}
	
	
	private void init(){
		
		singleWidth=(width-yLabelWidth)/count;
		singleHeight=(height-xLabelHeight)/count;
		
		
		for(int row=0;row<count;row++){
			for(int column=0;column<count;column++){
				subcharts[row][column]=new SingleChart(singleWidth, singleHeight, yLabelWidth+column*singleWidth, xLabelHeight+row*singleHeight, renderer, dataset.getAxis(column), dataset.getAxis(row));
				subcharts[row][column].getHorizontalAxis().setCaptionVisible(false);
				subcharts[row][column].getVerticalAxis().setCaptionVisible(false);
				subcharts[row][column].setZcolorer(dataset.getColorer(this.colorIndex));
				
				subcharts[row][column].setTickLabelVisible(false);
				subcharts[row][column].setMargin(10);
				subcharts[row][column].setLabelSize(15);
				
				subcharts[row][column].setBackGroundColor(Color.getHSBColor(0.0f, 0.0f, 0.95f));
				
				
				subcharts[row][column].startChart();
			}
		}
		
		String caption;
		
		for(int row=0;row<count;row++){
			caption = subcharts[row][0].getVerticalAxis().getCaption();
			renderer.drawText(
					caption,
					yLabelWidth/2,
					xLabelHeight + row*singleHeight+singleHeight/2,
					TextAlignment.Center,
					captionColor,
					-90,
					captionSize);
			
			
		}
		
		for(int column=0;column<count;column++){
			caption = subcharts[0][column].getHorizontalAxis().getCaption();
			renderer.drawText(
					caption,
					yLabelWidth+ column*singleWidth + singleWidth/2,
					xLabelHeight/2,
					TextAlignment.Center,
					captionColor,
					0,
					captionSize);
		}
	}
	
	Color captionColor=Color.GRAY;
	public Color getCaptionColor() {
		return captionColor;
	}

	public void setCaptionColor(Color captionColor) {
		this.captionColor = captionColor;
	}

	public int getCaptionSize() {
		return captionSize;
	}

	public void setCaptionSize(int captionSize) {
		this.captionSize = captionSize;
	}

	int captionSize=25;
	
	public void renderPoint(int row,int column,double x,double y,double z,int radius){
		subcharts[row][column].renderPoint(x, y, z, radius);
	}
	
	
	
	
}
