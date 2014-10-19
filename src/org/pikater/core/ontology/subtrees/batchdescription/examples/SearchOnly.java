package org.pikater.core.ontology.subtrees.batchdescription.examples;


import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newoption.values.QuestionMarkRange;

import java.util.ArrayList;
import java.util.List;


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
        ca.setTestingData(dsd);
        ca.setValidationData(dsd);
        ca.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
        ca.setEvaluationMethod(new EvaluationMethod(CrossValidation.class.getName()));
        //"whatever.mlp.is.in.MLP"

        List<NewOption> options = new ArrayList<NewOption>();
        NewOption lr = new NewOption("L", 
        		new QuestionMarkRange(
        				new DoubleValue(0.0), new DoubleValue(0.0)));
        
        NewOption hr = new NewOption( "H",4);
 
        options.add(lr);
        options.add(hr);

        ca.setOptions(options);

        CARecSearchComplex crsc = new CARecSearchComplex();
        crsc.setComputingAgent(ca);

        ErrorSourceDescription ed = new ErrorSourceDescription();
        ed.setProvider(ca);
        ed.setOutputType("mse");

        List<ErrorSourceDescription> eds = new ArrayList<ErrorSourceDescription>();
        eds.add(ed);

        crsc.setErrors(eds);

        Search sa = new Search();
        sa.setAgentType("whatever.ea.is.in.EA");

        List<NewOption> searchParameters = new ArrayList<NewOption>();
        
        NewOption pr = new NewOption(
        		 "ea.popSize",50);

        NewOption ear = new NewOption( "ea.mutationRate",0.03);

        searchParameters.add(pr);
        searchParameters.add(ear);

        crsc.setSearch(sa);

        DataSourceDescription cAds = new DataSourceDescription();
        cAds.setDataProvider(ca);
        cAds.setOutputType("trained");

        FileDataSaver fds = new FileDataSaver();
        fds.setDataSource(cAds);

        ComputationDescription cd = new ComputationDescription();
        cd.addRootElement(fds);

        return cd;
    }

}
