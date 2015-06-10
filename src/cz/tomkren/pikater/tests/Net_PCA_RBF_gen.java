package cz.tomkren.pikater.tests;

import cz.tomkren.helpers.Log;
import cz.tomkren.pikater.BoxUtils;
import org.pikater.core.experiments.ITestExperiment;
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;

/** Created by tom on 10. 6. 2015. */
public class Net_PCA_RBF_gen implements ITestExperiment {

    @Override
    public ComputationDescription createDescription() {
        return BoxUtils.mkComputationDescription("iris.arff",
                "0 input    0 2 1:0 3:1",
                "1 PCA      1 1 2:0",
                "2 RBF      1 1 3:0",
                "3 err      2 1 4:0",
                "4 output   1 0"
        );
    }

}
