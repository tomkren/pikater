package org.pikater.core.ontology.subtrees.batchDescription.examples;


import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;


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

        ComputingAgent ca = new ComputingAgent();
        ca.setTrainingData(dsd);
        ca.setModel(new NewModel());
        ca.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
        //"whatever.mlp.is.in.MLP"

        List<NewOption> options = new ArrayList<NewOption>();

        NewOption lr = new NewOption(
        		new DoubleValue(0.001), "L"); 
        
        NewOption hr = new NewOption(
        		new IntegerValue(4), "H"); 

        options.add(lr);
        options.add(hr);

        ca.setOptions(options);

        DataSourceDescription CAds = new DataSourceDescription();
        CAds.setDataProvider(ca);
        CAds.setDataOutputType("trained");

        FileDataSaver fds = new FileDataSaver();
        fds.setDataSource(CAds);

        ComputationDescription cd = new ComputationDescription();
        cd.addRootElement(fds);

        return cd;
    }

}
