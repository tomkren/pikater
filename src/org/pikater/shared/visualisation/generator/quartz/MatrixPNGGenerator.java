package org.pikater.shared.visualisation.generator.quartz;

import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.visualisation.charts.MatrixChart;
import org.pikater.shared.visualisation.datasource.multiple.MultipleArffDataset;
import org.pikater.shared.visualisation.generator.ChartGenerator;
import org.pikater.shared.visualisation.renderer.ImageRenderer;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs.IProgressDialogContextForJobs;

public class MatrixPNGGenerator extends Generator {
	
	private JPADataSetLO dslo;
	private MultipleArffDataset dataset;
	
	public MatrixPNGGenerator(IProgressDialogContextForJobs progressListener, JPADataSetLO dslo, PrintStream output){
		super(progressListener,output);
		this.dslo=dslo;
		init();
	}

	private void init(){
		this.dataset=new MultipleArffDataset(dslo);
		this.instNum=dataset.getNumberOfInstances();
	}
	
	
	@Override
	public void create() throws IOException {
		
		ImageRenderer ir=new ImageRenderer(ChartGenerator.MATRIX_CHART_SIZE, ChartGenerator.MATRIX_CHART_SIZE);
		ir.begin();


		MultipleArffDataset dataset=new MultipleArffDataset(dslo);

		int attrNum=dataset.getNumberOfAttributes();

		MatrixChart mchg=new MatrixChart(dataset, ChartGenerator.MATRIX_CHART_SIZE, ChartGenerator.MATRIX_CHART_SIZE, attrNum, ir, attrNum-1);

		try{
			while(dataset.next()){

				for(int row=0;row<attrNum;row++){
					for(int column=0;column<attrNum;column++){
						mchg.renderPoint(row, column, dataset.getAttributeValue(column), dataset.getAttributeValue(row), dataset.getAttributeValue(attrNum-1), 7);
					}
				}
				signalOneProcessedRow();
			}
		}finally{
			dataset.close();
		}

		ir.end();
		ImageIO.write(ir.getImage(), "PNG", output);
		output.close();
		signalFinish();
	}
	
	
	
}
