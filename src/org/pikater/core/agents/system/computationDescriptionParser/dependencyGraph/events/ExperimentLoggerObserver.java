package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.events;

import org.pikater.core.agents.PikaterAgent;

import java.util.Observable;
import java.util.Observer;

/**
 * User: Kuba
 * Date: 16.8.2014
 * Time: 18:41
 */
public class ExperimentLoggerObserver implements Observer{
    private PikaterAgent agent;

    public ExperimentLoggerObserver(PikaterAgent agent) {
        this.agent = agent;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ExperimentFinished)
        {
            ExperimentFinished finishedEvent= ((ExperimentFinished) arg);
            agent.log("*****FINISHED EXPERIMENT Id: "+finishedEvent.getExperimentId()+" *******");
        }
    }
}
