package org.pikater.web.visualisation.generator.quartz;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskContext;
import org.pikater.web.visualisation.charts.SingleChart;
import org.pikater.web.visualisation.charts.axis.Axis;
import org.pikater.web.visualisation.charts.coloring.Colorer;
import org.pikater.web.visualisation.charts.exception.ChartException;
import org.pikater.web.visualisation.datasource.single.ArffXYZPoint;
import org.pikater.web.visualisation.datasource.single.SingleArffDataset;
import org.pikater.web.visualisation.generator.ChartGenerator;
import org.pikater.web.visualisation.renderer.SVGRenderer;

public class ComparisonSVGGenerator extends Generator {

	SingleArffDataset dataset1;
	SingleArffDataset dataset2;
	
	
	public ComparisonSVGGenerator(IProgressDialogTaskContext progressListener,PrintStream output,JPADataSetLO dslo1,JPADataSetLO dslo2,int XIndex1, int XIndex2, int YIndex1, int YIndex2,int ColorIndex1,int ColorIndex2){
		super(progressListener,output);
		
		dataset1=new SingleArffDataset(dslo1, XIndex1, YIndex1, ColorIndex1);
		dataset2=new SingleArffDataset(dslo2, XIndex2, YIndex2, ColorIndex2);
		
		init();
	}
	
	
	public ComparisonSVGGenerator(IProgressDialogTaskContext progressHandler,PrintStream output,JPADataSetLO dslo1,JPADataSetLO dslo2,String XName1, String XName2, String YName1, String YName2,String ColorName1,String ColorName2){
		super(progressHandler,output);
		
		dataset1=new SingleArffDataset(dslo1, XName1, YName1, ColorName1);
		dataset2=new SingleArffDataset(dslo2, XName2, YName2, ColorName2);
		
		init();
	}
	
	private void init(){
		this.instNum=dataset1.getNumberOfInstances()+dataset2.getNumberOfInstances();
	}
	
	@Override
	public void create() throws IOException, ChartException {
		try{
			SVGRenderer svgr=new SVGRenderer(output, ChartGenerator.SINGLE_CHART_SIZE, ChartGenerator.SINGLE_CHART_SIZE);
			svgr.begin();

			Axis yAxis=Axis.join(dataset1.getYAxis(), dataset2.getYAxis());
			Axis xAxis=Axis.join(dataset1.getXAxis(), dataset2.getXAxis());
			Colorer colorer1=dataset1.getZColorer();
			Colorer colorer2=dataset2.getZColorer();

			Colorer colorer=colorer1.merge(colorer2);

			SingleChart sch=new SingleChart(ChartGenerator.SINGLE_CHART_SIZE, ChartGenerator.SINGLE_CHART_SIZE, svgr, xAxis, yAxis);
			sch.setMargin(50);
			sch.setLabelSize(150);
			sch.setBackGroundColor(Color.getHSBColor(0.0f, 0.0f, 0.95f));
			sch.setHorizontalCaptionColor(Color.GRAY);
			sch.setVerticalCaptionColor(Color.GRAY);
			sch.setCaptionSize(35);

			sch.startChart();

			ArffXYZPoint next=null;

			while((next=dataset1.getNext())!=null){
				sch.renderPoint(next.getX(), next.getY(), next.getZ(), 17, colorer);
				signalOneProcessedRow();
			}

			while((next=dataset2.getNext())!=null){
				sch.renderPoint(next.getX(), next.getY(), next.getZ(), 12, colorer);
				signalOneProcessedRow();
			}

			svgr.end();

		}finally{
			output.close();
			dataset1.close();
			dataset2.close();
		}
	}

}
