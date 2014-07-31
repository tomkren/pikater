package org.pikater.web.visualisation.implementation;

import java.util.List;
import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.utils.ResultFormatter;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.visualisation.definition.result.VisualizeDatasetResult;
import org.pikater.web.visualisation.implementation.charts.axis.exception.AxisNotJoinableException;
import org.pikater.web.visualisation.implementation.charts.coloring.exception.ColorerNotMergeableException;
import org.pikater.web.visualisation.implementation.generator.quartz.ComparisonSVGGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.MatrixPNGGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.SingleSVGGenerator;

public class TestVisual {

	public static void main(String[] args) throws IOException, AxisNotJoinableException, ColorerNotMergeableException {
		
		VisualizeDatasetResult dummyResult = new VisualizeDatasetResult(new IProgressDialogResultHandler()
		{
			@Override
			public void updateProgress(float percentage)
			{
				// TODO Auto-generated method stub
			}
			
			@Override
			public void finished(IProgressDialogTaskResult result)
			{
				// TODO Auto-generated method stub
			}
			
			@Override
			public void failed()
			{
				// TODO Auto-generated method stub
			}
		});
		
		long time=0;
		JPADataSetLO iris1=new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByDescription("iris")).getSingleResultWithNull();
		JPADataSetLO iris2=new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByDescription("iris2")).getSingleResultWithNull();
		
		String attr1="sepallength";
		String attr2="petallength";
		String attr3="class";
		
		System.out.println("Generating SVG Comparison Chart for: " +iris1.getDescription()+" and "+iris2.getDescription());
		time=System.currentTimeMillis();
		//ChartGenerator.generateSVGComparisonDatasetChart(iris, iris,new PrintStream("core/datasets/visual/sIRIS_sepallength_petallength_class_c.svg"), attr1, attr1, attr2, attr2,attr3, attr3);
		ComparisonSVGGenerator csvggiris=new ComparisonSVGGenerator(dummyResult, new PrintStream("core/datasets/visual/sIRIS_sepallength_petallength_class_c.svg"), iris1, iris2, attr1, attr1, attr2, attr2, attr3, attr3);
		csvggiris.create();
		System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
		
		
		List<JPADataSetLO> datasets = DAOs.dataSetDAO.getAll();
		
		for(JPADataSetLO dataset : datasets){
			
			System.out.println("Generating SVG Comparison Chart for: " +dataset.getDescription()+" and "+dataset.getDescription());
			time=System.currentTimeMillis();
			ComparisonSVGGenerator csvgg=new ComparisonSVGGenerator(dummyResult,new PrintStream("core/datasets/visual/"+dataset.getDescription()+"_0_1_2_comp.svg"),dataset, dataset, 0, 0, 1, 1, 2, 2);
			csvgg.create();
			System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
			
			System.out.println("Generating SVG Single Chart for: " +dataset.getDescription());
			time=System.currentTimeMillis();
			
			SingleSVGGenerator ssvgg=new SingleSVGGenerator(dummyResult, dataset,new PrintStream("core/datasets/visual/"+dataset.getDescription()+"_0_1_2s.svg"), 0, 1, 2);
			ssvgg.create();
			
			System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
			
			System.out.println("Generating PNG Matrix Chart for: " +dataset.getDescription());
			time=System.currentTimeMillis();
			MatrixPNGGenerator mpngg=new MatrixPNGGenerator(dummyResult, dataset, new PrintStream("core/datasets/visual/"+dataset.getDescription()+".png"));
			mpngg.create();
			
			System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
			
		}
	}
}