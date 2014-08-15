package org.pikater.web.visualisation.implementation;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.pglargeobject.PostgreLobAccess;
import org.pikater.shared.database.utils.ResultFormatter;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.visualisation.definition.result.DSVisOneResult;
import org.pikater.web.visualisation.implementation.charts.axis.exception.AxisNotJoinableException;
import org.pikater.web.visualisation.implementation.charts.coloring.exception.ColorerNotMergeableException;
import org.pikater.web.visualisation.implementation.generator.ChartGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.ComparisonSVGGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.SinglePNGGenerator;

public class TestVisual {

	public static void main(String[] args) throws IOException, AxisNotJoinableException, ColorerNotMergeableException {
		
		DSVisOneResult dummyResult = new DSVisOneResult(new IProgressDialogResultHandler()
		{
			@Override
			public void updateProgress(float percentage)
			{
			}
			
			@Override
			public void finished(IProgressDialogTaskResult result)
			{
			}
			
			@Override
			public void failed()
			{
			}
		}, ChartGenerator.SINGLE_CHART_SIZE, ChartGenerator.SINGLE_CHART_SIZE);
		
		long time=0;
		JPADataSetLO iris1=new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByDescription("iris")).getSingleResultWithNull();
		JPADataSetLO iris2=new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByDescription("iris")).getSingleResultWithNull();
		
		String attr1="sepallength";
		String attr2="petallength";
		String attr3="class";
		
		System.out.println("Generating SVG Comparison Chart for: " +iris1.getDescription()+" and "+iris2.getDescription());
		time=System.currentTimeMillis();
		try{
			File iris1file=PostgreLobAccess.downloadFileFromDB(iris1.getOID());
			File iris2file=PostgreLobAccess.downloadFileFromDB(iris2.getOID());
			System.out.println("Iris 1 temp file: "+iris1file.getAbsolutePath());
			System.out.println("Iris 2 temp file: "+iris2file.getAbsolutePath());
			ComparisonSVGGenerator csvggiris=new ComparisonSVGGenerator(dummyResult, new PrintStream("core/datasets/visual/sIRIS_sepallength_petallength_class_c.svg"), iris1, iris2, iris1file, iris2file, attr1, attr1, attr2, attr2, attr3, attr3);
			csvggiris.create();
		}catch(Throwable t){}
		System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
	
		
		List<JPADataSetLO> datasets = DAOs.dataSetDAO.getAll();
		
		for(JPADataSetLO dataset : datasets){
			
			System.out.println("Generating PNG Single Chart for: " +dataset.getDescription());
			time=System.currentTimeMillis();
			
			File temp;
			try {
				temp = PostgreLobAccess.downloadFileFromDB(dataset.getOID());
				System.out.println("Dataset downloaded to "+temp.getAbsolutePath());
				SinglePNGGenerator spngg=new SinglePNGGenerator(dummyResult, dataset,temp,new PrintStream("core/datasets/visual/"+dataset.getDescription()+"_0_1_2s.png"), 0, 1, 2);
				spngg.create();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
			break;
		}
	}
}
