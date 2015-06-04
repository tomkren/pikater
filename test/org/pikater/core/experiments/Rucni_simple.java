package org.pikater.core.experiments;

import cz.tomkren.helpers.Log;
import org.pikater.core.agents.experiment.dataprocessing.errorcomputing.Agent_Accuracy;
import org.pikater.core.ontology.subtrees.batchdescription.*;


/**
 * Single datasource -> single computing agent -> single save.
 *
 * @author Klara
 */
public final class Rucni_simple implements ITestExperiment {

    public static void main(String[] args) {
        Rucni_simple r = new Rucni_simple();
        ComputationDescription cd = r.createDescription();

        Log.it(cd.exportXML());
    }

    public ComputationDescription createDescription() {

        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");

        // Specify a datasource
        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

        DataProcessing err = new DataProcessing();
        err.setAgentType(Agent_Accuracy.class.getName());
        err.addDataSources(fileDataSource);
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

