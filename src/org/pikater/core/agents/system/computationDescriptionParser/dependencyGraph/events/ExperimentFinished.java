package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.events;

/**
 * User: Kuba
 * Date: 16.8.2014
 * Time: 18:00
 */
public class ExperimentFinished {
    private final int experimentId;

    public ExperimentFinished(int experimentId) {

        this.experimentId = experimentId;
    }

    public int getExperimentId() {
        return experimentId;
    }
}
