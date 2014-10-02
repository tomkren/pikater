package org.pikater.core.agents.system.computation.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.system.computation.graph.events.BatchFinished;

/**
 * User: Kuba
 * Date: 11.5.2014
 * Time: 21:27
 */
public class ComputationGraph extends Observable {
    private Map<Integer,ComputationNode> nodes = new HashMap<Integer,ComputationNode>();
    private int id;
    private int batchID;
    
    public ComputationGraph()
    {
        id = CoreConfiguration.getGUIDGenerator().getAndAllocateGUID();
    }
    
    public Map<Integer, ComputationNode> getNodes() {
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
            if (node.existsUnfinishedTasks())
            {
            	return;
            }
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
