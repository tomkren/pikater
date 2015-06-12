package cz.tomkren.pikater.tests;

import cz.tomkren.helpers.Log;
import cz.tomkren.pikater.BoxUtils;
import org.pikater.core.experiments.ITestExperiment;
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;

/** Created by tom on 10. 6. 2015. */
public class Net_PCA_RBF_gen implements ITestExperiment {

    public static void main(String[] args) {
        Log.it(new Net_PCA_RBF_gen().createDescription().exportXML());
    }

    @Override
    public ComputationDescription createDescription() {
        return BoxUtils.mkComputationDescription("weather.arff",
                "100    input    0 5    101:0 101:1 101:2 101:3 103:1",
                "101    PCA      4 4    102:0 102:1 102:2 102:3",
                "102    RBF      4 1    103:0",
                "103    err      2 1    104:0",
                "104    output   1 0"
        );
    }

}
