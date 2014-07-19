package org.pikater.shared.visualisation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import weka.core.Instances;
//TODO: add new weka to enable WekaOffscreenChartRenderer but fix RBFNetwork missing
//import weka.gui.beans.WekaOffscreenChartRenderer;

/**
 * This class can be used to generate a matrix of scatterplots for a dataset
 * Result is a PNG image. 
 */
public class SimpleVisualisator {
	static int TILE_WIDTH=300;
	static int TILE_HEIGHT=300;

	static int SPACE_BETWEEN_TABLES=50;
	static int HEADER_INDENT=20;

	private Instances getInstances(File file) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Instances data = new Instances(reader);
		reader.close();
		return data;
	}

	public void createComparisonScatteredPlot(File datasetFile1,File datasetFile2,File plotFile) throws Exception{
		List<Instances> instances=new ArrayList<Instances>();
		instances.add(this.getInstances(datasetFile1));
		instances.add(this.getInstances(datasetFile2));
		this.createScatteredPlot(instances, plotFile);
	}
	
	public void createScatteredPlot(File datasetFile,File plotFile) throws Exception{
		List<Instances> instances=new ArrayList<Instances>();
		instances.add(this.getInstances(datasetFile));
		this.createScatteredPlot(instances, plotFile);
	}

	private void createScatteredPlot(List<Instances> instances,File plotFile) throws Exception{	
		//WekaOffscreenChartRenderer wocr=new WekaOffscreenChartRenderer();

		String xAxis="";
		String yAxis="";

		List<String> options=new ArrayList<String>();
		//options.add("-title=["+xAxis+" -- "+yAxis+"]");
		options.add("-color=/last");
		//options.add("-shapeSize=3");

		Instances data=instances.get(0);//we just need one instences object from all compatible ones, to get some basic information e.g. attribute names

		int xSize=data.numAttributes()-1;
		int ySize=data.numAttributes()-1;

		BufferedImage[][] tiles= new BufferedImage[xSize][ySize];
		String[][] titles=new String[xSize][ySize];

		//n-1 attribute , coloring based on last attribute
		for(int x=0;x<data.numAttributes()-1;x++){
			for(int y=0;y<data.numAttributes()-1;y++){
				xAxis= data.attribute(x).name();
				yAxis= data.attribute(y).name();
				//tiles[x][y]=wocr.renderXYScatterPlot(TILE_WIDTH, TILE_HEIGHT, instances, xAxis, yAxis, options);
				titles[x][y]=data.attribute(x).name()+" vs "+data.attribute(y).name();
			}
		}


		int largeSizeWidth=xSize*TILE_WIDTH;
		int largeSizeHeight=ySize*TILE_HEIGHT+ySize*50;

		BufferedImage large=new BufferedImage(largeSizeWidth, largeSizeHeight,BufferedImage.TYPE_INT_RGB);

		for(int x=0;x<large.getWidth();x++){
			for(int y=0;y<large.getHeight();y++){
				large.setRGB(x, y, Integer.MAX_VALUE);
			}
		}

		for(int x=0;x<data.numAttributes()-1;x++){
			for(int y=0;y<data.numAttributes()-1;y++){

				int curX=x*TILE_WIDTH;
				int curY=y*TILE_HEIGHT+y*SPACE_BETWEEN_TABLES;

				for(int xs=0;xs<TILE_WIDTH;xs++){
					for(int ys=0;ys<TILE_HEIGHT;ys++){
						large.setRGB(curX+xs, curY+ys+SPACE_BETWEEN_TABLES, tiles[x][y].getRGB(xs, ys));

					}
				}				
			}
		}

		Graphics2D g2=large.createGraphics();
		g2.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));//start with white background
		g2.setFont(new Font("Arial",Font.BOLD,14));

		for(int x=0;x<data.numAttributes()-1;x++){
			for(int y=0;y<data.numAttributes()-1;y++){
				int curX=x*TILE_WIDTH;
				int curY=y*TILE_HEIGHT+y*SPACE_BETWEEN_TABLES;
				g2.drawString(titles[x][y], curX+HEADER_INDENT, curY+SPACE_BETWEEN_TABLES-10);
			}
		}


		ImageIO.write(large, "png", plotFile);
	}
}
