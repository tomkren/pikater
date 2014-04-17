package org.pikater.core.ontology.description.examples;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.*;
import org.pikater.core.ontology.messages.Option;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class SimpleTraining {

    //trains the MLP on iris and shows differences between correct and trained classification
    public static void createDescription() {

        FileDataProvider fdp = new FileDataProvider();
        fdp.setFileURI("iris.arff");
        DataSourceDescription dsd = new DataSourceDescription();
        dsd.setDataProvider(fdp);

        ComputingAgent ca = new ComputingAgent();
        ca.setTrainingData(dsd);
        ca.setModelClass(Agent_WekaCA.class.getName());
        //"whatever.mlp.is.in.MLP"

        ArrayList<Option> options = new ArrayList<>();
        
        StepanuvOption lr = new StepanuvOption();
        lr.setName("L");
        lr.setOption(new OptionValue(new Double(0.001)) );

        StepanuvOption hr = new StepanuvOption();
        hr.setName("H");
        hr.setOption(new OptionValue(new Integer(4)) );
        
        options.add(lr.toOption());
        options.add(hr.toOption());

        ca.setOptions(options);

        DataSourceDescription CAds = new DataSourceDescription();
        CAds.setDataProvider(ca);
        CAds.setDataType("trained");

        DifferenceVisualizer diff = new DifferenceVisualizer();
        diff.setTargetData(dsd);
        diff.setModelData(CAds);

        ComputationDescription cd = new ComputationDescription();
        cd.setRootElement(diff);

    }

}
