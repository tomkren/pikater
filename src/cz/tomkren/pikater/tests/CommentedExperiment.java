package cz.tomkren.pikater.tests;

import org.pikater.core.experiments.ITestExperiment;
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;

/** Created by tom on 9. 6. 2015.*/
public class CommentedExperiment implements ITestExperiment  {

    private final ITestExperiment experiment;
    private final String comment;

    public CommentedExperiment(String comment, ITestExperiment experiment) {
        this.experiment = experiment;
        this.comment = comment;
    }

    @Override
    public ComputationDescription createDescription() {
        return experiment.createDescription();
    }

    public ITestExperiment getExperiment() {
        return experiment;
    }

    public String getComment() {
        return comment;
    }
}
