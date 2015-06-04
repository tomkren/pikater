package org.pikater.core.experiments;

import org.pikater.core.agents.experiment.computing.Agent_WekaMultilayerPerceptronCA;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_KMeans;
import org.pikater.core.agents.experiment.dataprocessing.Agent_PCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_RomanovoU;
import org.pikater.core.agents.experiment.dataprocessing.errorcomputing.Agent_Accuracy;
import org.pikater.core.ontology.subtrees.batchdescription.*;

/**
 * Single datasource -> single computing agent -> single save.
 * 
 * @author Klara
 */
public final class Input_Tom implements ITestExperiment {

	public ComputationDescription createDescription() {

		FileDataProvider fileDataProvider = new FileDataProvider();
		fileDataProvider.setFileURI("weather.arff");

		// Specify a datasource
		DataSourceDescription fileDataSource = new DataSourceDescription();
		fileDataSource.setDataProvider(fileDataProvider);


		// PreProcessing

		// PCA
		DataProcessing pca = new DataProcessing();
		pca.setAgentType(Agent_PCA.class.getName());
		pca.addDataSources(fileDataSource);

		// Data processed by pca are the new datasource
		DataSourceDescription dataSourcePCA = new DataSourceDescription();
		dataSourcePCA.setOutputType("Data");
		dataSourcePCA.setDataProvider(pca);


		// k-means
		DataProcessing kmeans = new DataProcessing();
		kmeans.setAgentType(Agent_KMeans.class.getName());
		kmeans.addDataSources(dataSourcePCA);

		// Data processed by k-means are the new datasource
		DataSourceDescription dataSourceKMeans1 = new DataSourceDescription();
		dataSourceKMeans1.setOutputType("Output_1");
		// dataSourceSunny.setInputType(CoreConstant.SlotContent.TRAINING_DATA.getSlotName());
		dataSourceKMeans1.setDataProvider(kmeans);

		DataSourceDescription dataSourceKMeans2 = new DataSourceDescription();
		dataSourceKMeans2.setOutputType("Output_2");
		// dataSourceSunny.setInputType(CoreConstant.SlotContent.TRAINING_DATA.getSlotName());
		dataSourceKMeans2.setDataProvider(kmeans);


		// Create new computing agent, add options and datasource that we have
		// created above
		ComputingAgent rbf = new ComputingAgent();
		rbf.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		rbf.setTrainingData(dataSourceKMeans1);
		rbf.setTestingData(dataSourceKMeans1);
		rbf.setValidationData(dataSourceKMeans1);
		rbf.setDataToLabel(dataSourceKMeans1);

		// TODO
		/* rbf.setEvaluationMethod(new EvaluationMethod(
				CrossValidation.class.getName()));
		rbf.setEvaluationMethod(evaluationMethod);
		*/

		ComputingAgent mlp = new ComputingAgent();
		mlp.setAgentType(Agent_WekaMultilayerPerceptronCA.class.getName());
		mlp.setTrainingData(dataSourceKMeans2);
		mlp.setTestingData(dataSourceKMeans2);
		mlp.setValidationData(dataSourceKMeans2);
		mlp.setDataToLabel(dataSourceKMeans2);


		// Labeled data labeled by our CA are the new datasource
		DataSourceDescription labeledDataSourceRBF = new DataSourceDescription();
		labeledDataSourceRBF.setOutputType("Data");
		labeledDataSourceRBF.setDataProvider(rbf);

		DataSourceDescription labeledDataSourceMLP = new DataSourceDescription();
		labeledDataSourceMLP.setOutputType("Data");
		labeledDataSourceMLP.setDataProvider(mlp);

		DataProcessing u = new DataProcessing();
		u.setAgentType(Agent_RomanovoU.class.getName());
		u.addDataSources(labeledDataSourceRBF);
		u.addDataSources(labeledDataSourceMLP);


		DataSourceDescription labeledDataSourceAll = new DataSourceDescription();
		labeledDataSourceAll.setOutputType("Data");
		labeledDataSourceAll.setDataProvider(u);


		ComputingAgent err = new ComputingAgent();
		err.setAgentType(Agent_Accuracy.class.getName());
		err.setTrainingData(labeledDataSourceAll);
		err.setValidationData(fileDataSource);

		// error output is the new datasource
		DataSourceDescription errDataSource = new DataSourceDescription();
		errDataSource.setOutputType("Error");
		errDataSource.setDataProvider(err);

		// Save labeled data
		FileDataSaver saver = new FileDataSaver();
		saver.setDataSource(errDataSource);

		ComputationDescription comDescription = new ComputationDescription();
		comDescription.addRootElement(saver);

		return comDescription;
	}
}
