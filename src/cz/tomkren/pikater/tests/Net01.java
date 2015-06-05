package cz.tomkren.pikater.tests;

import cz.tomkren.helpers.Log;
import cz.tomkren.pikater.BoxUtils;
import org.pikater.core.experiments.ITestExperiment;
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;

/** Created by tom on 5. 6. 2015. */

public class Net01 implements ITestExperiment {

    @Override
    public ComputationDescription createDescription() {
        return BoxUtils.mkComputationDescription(
                "0 input    0 2 1:0 5:1",
                "1 k-means  1 2 2:0 3:0",
                "2 RBF      1 1 4:0",
                "3 RBF      1 1 4:1",
                "4 U        2 1 5:0",
                "5 err      2 1 6:0",
                "6 output   1 0"
        );
    }

}
