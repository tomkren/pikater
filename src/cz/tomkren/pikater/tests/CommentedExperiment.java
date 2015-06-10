package cz.tomkren.pikater.tests;

import org.pikater.core.experiments.ITestExperiment;

/** Created by tom on 9. 6. 2015.*/
public class CommentedExperiment {

    private final ITestExperiment experiment;
    private final String comment;

    public CommentedExperiment(ITestExperiment experiment, String comment) {
        this.experiment = experiment;
        this.comment = comment;
    }

    public CommentedExperiment(String comment, ITestExperiment experiment) {
        this.experiment = experiment;
        this.comment = comment;
    }

    public ITestExperiment getExperiment() {
        return experiment;
    }

    public String getComment() {
        return comment;
    }
}
