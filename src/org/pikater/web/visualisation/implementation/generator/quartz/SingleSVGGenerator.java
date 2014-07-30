package org.pikater.web.visualisation.implementation.generator.quartz;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.visualisation.definition.result.AbstractDatasetVisualizationResult;
import org.pikater.web.visualisation.implementation.charts.SingleChart;
import org.pikater.web.visualisation.implementation.charts.axis.Axis;
import org.pikater.web.visualisation.implementation.charts.coloring.Colorer;
import org.pikater.web.visualisation.implementation.datasource.single.ArffXYZPoint;
import org.pikater.web.visualisation.implementation.datasource.single.SingleArffDataset;
import org.pikater.web.visualisation.implementation.generator.ChartGenerator;
import org.pikater.web.visualisation.implementation.renderer.SVGRenderer;

public class SingleSVGGenerator extends Generator {

	private SingleArffDataset dataset;
	
	public SingleSVGGenerator(AbstractDatasetVisualizationResult progressListener, JPADataSetLO dslo,PrintStream output, int XIndex, int YIndex, int ColorIndex){
		super(progressListener,output);
		this.dataset=new SingleArffDataset(dslo, XIndex, YIndex, ColorIndex);
		init();
	}
	
	public SingleSVGGenerator(AbstractDatasetVisualizationResult progressListener, JPADataSetLO dslo,PrintStream output, String XName, String YName, String ColorName){
		super(progressListener,output);
		this.dataset=new SingleArffDataset(dslo, XName, YName, ColorName);
		init();
	}
	
	private void init(){
		this.instNum=dataset.getNumberOfInstances();
	}
	
	@Override
	public void create() throws IOException {
		SVGRenderer renderer=new SVGRenderer(output, ChartGenerator.SINGLE_CHART_SIZE, ChartGenerator.SINGLE_CHART_SIZE);
		renderer.begin();
		
		Axis yAxis=dataset.getYAxis();
		Axis xAxis=dataset.getXAxis();
		Colorer colorer=dataset.getZColorer();

		SingleChart sch=new SingleChart(ChartGenerator.SINGLE_CHART_SIZE, ChartGenerator.SINGLE_CHART_SIZE, renderer, xAxis, yAxis);
		sch.setMargin(50);
		sch.setLabelSize(150);
		sch.setBackGroundColor(Color.getHSBColor(0.0f, 0.0f, 0.95f));
		sch.setHorizontalCaptionColor(Color.GRAY);
		sch.setVerticalCaptionColor(Color.GRAY);
		sch.setCaptionSize(35);

		sch.startChart();

		ArffXYZPoint next=null;

		
		try{
		while((next=dataset.getNext())!=null){
			sch.renderPoint(next.getX(), next.getY(), next.getZ(), 15, colorer);
			signalOneProcessedRow();
		}
		}finally{
			dataset.close();
		}
		
		renderer.end();
		output.close();
	}

}
