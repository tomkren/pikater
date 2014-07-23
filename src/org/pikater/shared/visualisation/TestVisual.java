package org.pikater.shared.visualisation;

import java.util.List;
import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.utils.ResultFormatter;
import org.pikater.shared.visualisation.charts.axis.exception.AxisNotJoinableException;
import org.pikater.shared.visualisation.charts.coloring.exception.ColorerNotMergeableException;
import org.pikater.shared.visualisation.generator.ChartGenerator;
import org.pikater.shared.visualisation.generator.quartz.MatrixPNGGenerator;
import org.pikater.shared.visualisation.generator.quartz.SingleSVGGenerator;

public class TestVisual {

	public static void main(String[] args) throws IOException, AxisNotJoinableException, ColorerNotMergeableException {
		long time=0;
		JPADataSetLO iris=new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByDescription("iris")).getSingleResultWithNull();
		
		String attr1="sepallength";
		String attr2="petallength";
		String attr3="class";
		
		System.out.println("Generating SVG Comparison Chart for: " +iris.getDescription()+" and "+iris.getDescription());
		time=System.currentTimeMillis();
		ChartGenerator.generateSVGComparisonDatasetChart(iris, iris,new PrintStream("core/datasets/visual/sIRIS_sepallength_petallength_class_c.svg"), attr1, attr1, attr2, attr2,attr3, attr3);
		System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
		/**
		System.out.println("Generating SVG Single Chart for: " +iris.getDescription());
		time=System.currentTimeMillis();
		ChartGenerator.generateSVGSingleDatasetChart(
				iris,
				new PrintStream("core/datasets/visual/sIRIS"+attr1+"_"+attr2+"_"+attr3+"s.svg"), 
				attr1, 
				attr2,
				attr3,
				new DummyProgress());
		System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
		
		**/
		
		List<JPADataSetLO> datasets = DAOs.dataSetDAO.getAll();
		
		for(JPADataSetLO dataset : datasets){
			/**
			System.out.println("Generating SVG Comparison Chart for: " +dataset.getDescription()+" and "+dataset.getDescription());
			time=System.currentTimeMillis();
			ChartGenerator.generateSVGComparisonDatasetChart(dataset, dataset,new PrintStream("core/datasets/visual/"+dataset.getDescription()+"_0_1_2_comp.svg"), 0, 0, 1, 1, 2, 2);
			System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
			**/
			System.out.println("Generating SVG Single Chart for: " +dataset.getDescription());
			time=System.currentTimeMillis();
			/**
			ChartGenerator.generateSVGSingleDatasetChart(
					dataset,
					new PrintStream("core/datasets/visual/"+dataset.getDescription()+"_0_1_2s.svg"), 
					0, 
					1,
					2,
					new DummyProgress());**/
			SingleSVGGenerator ssvgg=new SingleSVGGenerator(new DummyProgress(), dataset,new PrintStream("core/datasets/visual/"+dataset.getDescription()+"_0_1_2s.svg"), 0, 1, 2);
			ssvgg.create();
			
			System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
			
			System.out.println("Generating PNG Matrix Chart for: " +dataset.getDescription());
			time=System.currentTimeMillis();
			//ChartGenerator.generatePNGMatrixDatasetChart(dataset, new PrintStream("core/datasets/visual/"+dataset.getDescription()+".png"));
			MatrixPNGGenerator mpngg=new MatrixPNGGenerator(new DummyProgress(), dataset, new PrintStream("core/datasets/visual/"+dataset.getDescription()+".png"));
			mpngg.create();
			
			System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
			
			/**
			System.out.println("Generating SVG Matrix Chart for: " +dataset.getDescription());
			time=System.currentTimeMillis();
			ChartGenerator.generateSVGMatrixDatasetChart(dataset, new PrintStream("core/datasets/visual/"+dataset.getDescription()+".svg"));
			System.out.println("Finished in: "+(System.currentTimeMillis()-time)+" ms");
			**/
		}
	}
}
