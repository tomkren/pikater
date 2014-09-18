package org.pikater.core.agents.system.computation.graph.events;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.shared.database.jpa.status.JPABatchStatus;

import java.util.Observable;
import java.util.Observer;

/**
 * User: Kuba
 * Date: 16.8.2014
 * Time: 18:41
 */
public class LoggerObserver implements Observer{
    private PikaterAgent agent;

    public LoggerObserver(PikaterAgent agent) {
        this.agent = agent;
    }

    @Override
    public void update(Observable o, Object arg) {
    	
        if (arg instanceof BatchFinished)
        {
            BatchFinished finishedEvent= ((BatchFinished) arg);
            int batchID = finishedEvent.getBatchID();
            
            DataManagerService.updateBatchStatus(
            		agent, batchID, JPABatchStatus.FINISHED.name());
            agent.logInfo("*****FINISHED BATCH ID: " + batchID + " *******");
        }
    }
}
