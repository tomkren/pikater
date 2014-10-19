package org.pikater.core.ontology.subtrees.batchdescription.examples;


import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.ontology.subtrees.batchdescription.duration.ExpectedDuration;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class SimpleTraining {

    //trains the MLP on iris and shows differences between correct and trained classification
    public static ComputationDescription createDescription() {

        FileDataProvider fdp = new FileDataProvider();
        fdp.setFileURI("iris.arff");
        DataSourceDescription dsd = new DataSourceDescription();
        dsd.setDataProvider(fdp);

        String durationString = ExpectedDuration.DurationType.SECONDS.toString();
        ExpectedDuration expectedDuration = new ExpectedDuration();
        expectedDuration.setDurationType(durationString);
        
        ComputingAgent ca = new ComputingAgent();
        ca.setTrainingData(dsd);
        ca.setTestingData(dsd);
        ca.setValidationData(dsd);
        ca.setDuration(expectedDuration);
        ca.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
        //"whatever.mlp.is.in.MLP"

        List<NewOption> options = new ArrayList<NewOption>();

        NewOption lr = new NewOption( "L",0.001);
        
        NewOption hr = new NewOption("H",4);

        options.add(lr);
        options.add(hr);

        ca.setOptions(options);

        DataSourceDescription CAds = new DataSourceDescription();
        CAds.setDataProvider(ca);
        CAds.setOutputType("trained");

        FileDataSaver fds = new FileDataSaver();
        fds.setDataSource(CAds);

        ComputationDescription cd = new ComputationDescription();
        cd.addRootElement(fds);

        return cd;
    }

}
