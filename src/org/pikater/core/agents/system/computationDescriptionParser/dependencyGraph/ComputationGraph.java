package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import java.util.HashMap;
import java.util.Observable;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.events.BatchFinished;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: Kuba
 * Date: 11.5.2014
 * Time: 21:27
 */
public class ComputationGraph extends Observable {
    private HashMap<Integer,ComputationNode> nodes=new HashMap<Integer,ComputationNode>();
    private int id;
    private int batchID;
    
    public ComputationGraph()
    {
        String initBeansName = CoreConfiguration.BEANS_CONFIG_FILE;
        @SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(initBeansName);
        GUIDGenerator generator= (GUIDGenerator) context.getBean("guidGenerator");
        id=generator.getAndAllocateGUID();
    }
    
    public HashMap<Integer, ComputationNode> getNodes() {
        return nodes;
    }

    public ComputationNode getNode(Integer nodeId)
    {
        return nodes.get(nodeId);
    }
    public void addNode(ComputationNode node)
    {
        nodes.put(node.getId(),node);
    }
    public void startBatchComputation()
    {
           for (ComputationNode node:nodes.values())
           {
               if (node.canComputationStart())
               {
                   node.startComputation();
               }
           }
    }

    public void computationBatchFinished()
    {
        BatchFinished finishedEvent = new BatchFinished(batchID);
        setChanged();
        notifyObservers(finishedEvent);
    }

    public void updateState()
    {
        for (ComputationNode node:nodes.values())
        {
            if (node.AnyOutstandingTasks()) return;
        }
        computationBatchFinished();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBatchID() {
        return batchID;
    }

    public void setBatchID(int batchID) {
        this.batchID = batchID;
    }
}
