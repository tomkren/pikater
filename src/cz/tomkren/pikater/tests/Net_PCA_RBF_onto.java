package cz.tomkren.pikater.tests;

import cz.tomkren.helpers.Log;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_PCA;
import org.pikater.core.agents.experiment.dataprocessing.errorcomputing.Agent_Accuracy;
import org.pikater.core.experiments.ITestExperiment;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.Standard;

/**
 * Single datasource -> single computing agent -> single save.
 *
 * @author tom
 */
public final class Net_PCA_RBF_onto implements ITestExperiment {

    public static void main(String[] args) {
        Net_PCA_RBF_onto r = new Net_PCA_RBF_onto();
        ComputationDescription cd = r.createDescription();
        Log.it(cd.exportXML());
    }

    public ComputationDescription createDescription() {

        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("iris.arff");

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

        // Create new computing agent, add options and datasource that we have
        // created above
        ComputingAgent rbf = new ComputingAgent();
        rbf.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
        rbf.setTrainingData  (dataSourcePCA);
        rbf.setTestingData   (dataSourcePCA);
        rbf.setValidationData(dataSourcePCA);
        rbf.setDataToLabel   (dataSourcePCA);
        rbf.setEvaluationMethod(new EvaluationMethod(Standard.class.getName()));


        // TODO
		/* rbf.setEvaluationMethod(new EvaluationMethod(
				CrossValidation.class.getName()));
		rbf.setEvaluationMethod(evaluationMethod);
		*/

        // Labeled data labeled by our CA are the new datasource
        DataSourceDescription labeledDataSourceRBF = new DataSourceDescription();
        labeledDataSourceRBF.setOutputType("Data");
        labeledDataSourceRBF.setDataProvider(rbf);

        // compute error
        DataProcessing err = new DataProcessing();
        err.setAgentType(Agent_Accuracy.class.getName());
        err.addDataSources(labeledDataSourceRBF);
        err.addDataSources(fileDataSource);

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
