package org.pikater.core.ontology.subtrees.batchDescription.examples;


import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.dataStructures.options.Converter;
import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.batchDescription.*;
import org.pikater.core.ontology.subtrees.option.Option;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class SearchOnly {

    public static ComputationDescription createDescription() {

        FileDataProvider fdp = new FileDataProvider();
        fdp.setFileURI("iris.arff");
        DataSourceDescription dsd = new DataSourceDescription();
        dsd.setDataProvider(fdp);

        ComputingAgent ca = new ComputingAgent();
        ca.setTrainingData(dsd);
        ca.setModelClass(Agent_WekaRBFNetworkCA.class.getName());
        //"whatever.mlp.is.in.MLP"

        List<Option> options = new ArrayList<Option>();
        StepanuvOption lr = new StepanuvOption();
        lr.setName("L");
        lr.setOption(new OptionInterval(new Double(0.0) ,new Double(1.0)));
 
        StepanuvOption hr = new StepanuvOption();
        hr.setName("H");
        hr.setOption(new OptionValue(new Integer(4)) );
 
        options.add(Converter.toOption(lr));
        options.add(Converter.toOption(hr));

        ca.setOptions(options);

        CARecSearchComplex crsc = new CARecSearchComplex();
        crsc.setComputingAgent(ca);

        ErrorDescription ed = new ErrorDescription();
        ed.setProvider(ca);
        ed.setType("mse");

        List<ErrorDescription> eds = new ArrayList<ErrorDescription>();
        eds.add(ed);

        crsc.setErrors(eds);

        Search sa = new Search();
        sa.setSearchClass("whatever.ea.is.in.EA");

        List<Option> searchParameters = new ArrayList<Option>();
        
        StepanuvOption pr = new StepanuvOption();
        pr.setName("ea.popSize");
        pr.setOption(new OptionValue(new Integer(50)) );
        
        StepanuvOption ear = new StepanuvOption();
        ear.setName("ea.mutationRate");
        ear.setOption(new OptionValue(new Double(0.03)) );

        searchParameters.add(Converter.toOption(pr));
        searchParameters.add(Converter.toOption(ear));

        crsc.setSearch(sa);

        DataSourceDescription CAds = new DataSourceDescription();
        CAds.setDataProvider(ca);
        CAds.setDataType("trained");

        FileDataSaver fds = new FileDataSaver();
        fds.setDataSource(CAds);

        ComputationDescription cd = new ComputationDescription();
        cd.addRootElement(fds);

        return cd;
    }

}
