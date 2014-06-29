package org.pikater.shared.utilities.pikaterDatabase.tests;

import java.io.File;

import org.pikater.shared.visualisation.SimpleVisualisator;

public class DataSetVisualisationTester {

	public static void main(String[] args) throws Exception {
		SimpleVisualisator sv=new SimpleVisualisator();
		sv.createScatteredPlot(
				new File("core/datasets/iris.arff"),
				new File("core/datasets/image/iris.png"));
		sv.createScatteredPlot(
				new File("core/datasets/adult.arff"),
				new File("core/datasets/image/adult.png"));
		sv.createScatteredPlot(
				new File("core/datasets/weather.arff"),
				new File("core/datasets/image/weather.png"));
		sv.createScatteredPlot(
				new File("core/datasets/autos.arff"),
				new File("core/datasets/image/autos.png"));
	}

}
