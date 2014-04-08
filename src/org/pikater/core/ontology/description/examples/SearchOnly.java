package org.pikater.core.ontology.description.examples;

import org.pikater.core.ontology.description.*;

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
        ca.setModelClass("whatever.mlp.is.in.MLP");

        ArrayList<Parameter> parameters = new ArrayList<>();
        DoubleParameter lr = new DoubleParameter();
        lr.setName("L");
        lr.setSearchable(true);
        lr.setRange(new Interval(0.0,1.0));
        parameters.add(lr);
        parameters.add(new IntegerParameter("H", 4));

        ca.setParameters(parameters);

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
        ArrayList<Parameter> searchParameters = new ArrayList<>();
        searchParameters.add(new IntegerParameter("ea.popSize", 50));
        searchParameters.add(new DoubleParameter("ea.mutationRate", 0.03));

        crsc.setSearch(sa);

        ComputationDescription cd = new ComputationDescription();
        cd.setRootElement(ca);


    }

}
