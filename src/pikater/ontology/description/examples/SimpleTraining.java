package pikater.ontology.description.examples;

import pikater.ontology.description.*;

import java.util.ArrayList;

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
        ca.setModelClass("whatever.mlp.is.in.MLP");

        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(new DoubleParameter("L", 0.001));
        parameters.add(new IntegerParameter("H", 4));

        ca.setParameters(parameters);

        DataSourceDescription CAds = new DataSourceDescription();
        CAds.setDataProvider(ca);
        CAds.setDataType("trained");

        DifferenceVisualizer diff = new DifferenceVisualizer();
        diff.setTargetData(dsd);
        diff.setModelData(CAds);

        ComputationDescription cd = new ComputationDescription();
        cd.setRootElement(diff);

        return cd;
    }

}
