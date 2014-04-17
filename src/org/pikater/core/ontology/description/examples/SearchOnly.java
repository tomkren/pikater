package org.pikater.core.ontology.description.examples;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.*;
import org.pikater.core.ontology.messages.Option;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class SearchOnly {

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
        lr.setOption(new OptionInterval(new Double(0.0) ,new Double(1.0)));
 
        StepanuvOption hr = new StepanuvOption();
        hr.setName("H");
        hr.setOption(new OptionValue(new Integer(4)) );
 
        options.add(lr.toOption());
        options.add(hr.toOption());

        ca.setOptions(options);

        CARecSearchComplex crsc = new CARecSearchComplex();
        crsc.setComputingAgent(ca);

        ErrorDescription ed = new ErrorDescription();
        ed.setProvider(ca);
        ed.setType("mse");

        ArrayList<ErrorDescription> eds = new ArrayList<>();
        eds.add(ed);

        crsc.setErrors(eds);

        Search sa = new Search();
        sa.setSearchClass("whatever.ea.is.in.EA");

        ArrayList<Option> searchParameters = new ArrayList<>();
        
        StepanuvOption pr = new StepanuvOption();
        pr.setName("ea.popSize");
        pr.setOption(new OptionValue(new Integer(50)) );
        
        StepanuvOption ear = new StepanuvOption();
        ear.setName("ea.mutationRate");
        ear.setOption(new OptionValue(new Double(0.03)) );

        searchParameters.add(pr.toOption());
        searchParameters.add(ear.toOption());

        crsc.setSearch(sa);

        ComputationDescription cd = new ComputationDescription();
        cd.setRootElement(ca);


    }

}
