package cz.tomkren.pikater.tests;

import cz.tomkren.helpers.Log;
import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_PCA;
import org.pikater.core.agents.experiment.dataprocessing.errorcomputing.Agent_Accuracy;
import org.pikater.core.experiments.ITestExperiment;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.Standard;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;

/** @author tom */

public final class Net_PCA_RBF_onto implements ITestExperiment {

    public static void main(String[] args) {
        Net_PCA_RBF_onto net = new Net_PCA_RBF_onto();
        ComputationDescription cd = net.createDescription();
        Log.it(cd.exportXML());
    }

    @Override
    public ComputationDescription createDescription() {

        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setId(100);
        fileDataProvider.setFileURI("weather.arff");

        // Specify a datasource
        DataSourceDescription fileDataSource1 = new DataSourceDescription();
        fileDataSource1.setDataProvider(fileDataProvider);
        fileDataSource1.setInputType(CoreConstant.SlotContent.TRAINING_DATA.getSlotName());

        DataSourceDescription fileDataSource2 = new DataSourceDescription();
        fileDataSource2.setDataProvider(fileDataProvider);
        fileDataSource2.setInputType(CoreConstant.SlotContent.TESTING_DATA.getSlotName());

        DataSourceDescription fileDataSource3 = new DataSourceDescription();
        fileDataSource3.setDataProvider(fileDataProvider);
        fileDataSource3.setInputType(CoreConstant.SlotContent.VALIDATION_DATA.getSlotName());

        DataSourceDescription fileDataSource4 = new DataSourceDescription();
        fileDataSource4.setDataProvider(fileDataProvider);
        fileDataSource4.setInputType(CoreConstant.SlotContent.DATA_TO_LABEL.getSlotName());

        // PreProcessing

        // PCA

        DataProcessing pca = new DataProcessing();
        pca.setId(101);
        pca.setAgentType(Agent_PCA.class.getName());
        pca.addOption(new NewOption("M",3)); // Maximum number of PC attributes to retain (-1 = include all, default: -1)

        pca.addDataSources(fileDataSource1);
        pca.addDataSources(fileDataSource2);
        pca.addDataSources(fileDataSource3);
        pca.addDataSources(fileDataSource4);

        // Data processed by pca are the new datasource
        DataSourceDescription dataSourcePCA1 = new DataSourceDescription();
        dataSourcePCA1.setOutputType(CoreConstant.SlotContent.TRAINING_DATA.getSlotName());
        dataSourcePCA1.setInputType(CoreConstant.SlotContent.TRAINING_DATA.getSlotName());
        dataSourcePCA1.setDataProvider(pca);

        DataSourceDescription dataSourcePCA2 = new DataSourceDescription();
        dataSourcePCA2.setOutputType(CoreConstant.SlotContent.TESTING_DATA.getSlotName());
        dataSourcePCA2.setInputType(CoreConstant.SlotContent.TESTING_DATA.getSlotName());
        dataSourcePCA2.setDataProvider(pca);

        DataSourceDescription dataSourcePCA3 = new DataSourceDescription();
        dataSourcePCA3.setOutputType(CoreConstant.SlotContent.VALIDATION_DATA.getSlotName());
        dataSourcePCA3.setInputType(CoreConstant.SlotContent.VALIDATION_DATA.getSlotName());
        dataSourcePCA3.setDataProvider(pca);

        DataSourceDescription dataSourcePCA4 = new DataSourceDescription();
        dataSourcePCA4.setOutputType(CoreConstant.SlotContent.DATA_TO_LABEL.getSlotName());
        dataSourcePCA4.setInputType(CoreConstant.SlotContent.DATA_TO_LABEL.getSlotName());
        dataSourcePCA4.setDataProvider(pca);


        // Create new computing agent, add options and datasource that we have
        // created above
        ComputingAgent rbf = new ComputingAgent();
        rbf.setId(102);
        rbf.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
        rbf.setTrainingData(dataSourcePCA1);
        rbf.setTestingData(dataSourcePCA2);
        rbf.setValidationData(dataSourcePCA3);
        rbf.setDataToLabel(dataSourcePCA4);

        rbf.setEvaluationMethod(new EvaluationMethod(Standard.class.getName()));

        // Labeled data labeled by our CA are the new datasource
        DataSourceDescription labeledDataSourceRBF = new DataSourceDescription();
        labeledDataSourceRBF.setInputType(CoreConstant.SlotContent.COMPUTED_DATA.getSlotName());
        labeledDataSourceRBF.setOutputType(CoreConstant.SlotContent.COMPUTED_DATA.getSlotName());
        labeledDataSourceRBF.setDataProvider(rbf);

        // compute error
        DataProcessing err = new DataProcessing();
        err.setId(103);
        err.setAgentType(Agent_Accuracy.class.getName());
        err.addDataSources(labeledDataSourceRBF);
        err.addDataSources(fileDataSource4);

        // error output is the new datasource
        DataSourceDescription errDataSource = new DataSourceDescription();
        errDataSource.setOutputType("Error");
        errDataSource.setDataProvider(err);

        // Save labeled data
        FileDataSaver saver = new FileDataSaver();
        saver.setId(104);
        saver.setDataSource(errDataSource);

        ComputationDescription comDescription = new ComputationDescription();
        comDescription.addRootElement(saver);

        return comDescription;
    }
}
