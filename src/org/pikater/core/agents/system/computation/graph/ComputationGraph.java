package org.pikater.core.agents.system.computation.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.system.computation.graph.events.BatchFinished;

/**
 * Describes batch holding sets of nodes representing each part of the graph
 * Is observable
 * User: Kuba
 * Date: 11.5.2014
 * Time: 21:27
 */
public class ComputationGraph extends Observable {
	
    private Map<Integer,ComputationNode> nodes = new HashMap<>();
    private int id;
    private int batchID;
    
    public ComputationGraph() {
        id = CoreConfiguration.getGUIDGenerator().getAndAllocateGUID();
    }

    /**
     * Gets list of computation nodes in the graph
     * @return Computation nodes in graph
     */
    public Map<Integer, ComputationNode> getNodes() {
        return nodes;
    }

    /**
     * Gets node by id
     * @param nodeId  Id of the node
     * @return Computation node with the ID
     */
    public ComputationNode getNode(Integer nodeId) {
        return nodes.get(nodeId);
    }

    /**
     * Add node to the graph
     * @param node Node to be added
     */
    public void addNode(ComputationNode node) {
        nodes.put(node.getId(),node);
    }

    /**
     * Starts computation - try to start computation of each node
     */
    public void startBatchComputation() {
    	
       for (ComputationNode node:nodes.values()) {
           if (node.canComputationStart()) {
               node.startComputation();
           }
       }
    }

    /**
     * No node is computing or waiting for something - batch is finished
     */
    public void computationBatchFinished() {
        BatchFinished finishedEvent = new BatchFinished(batchID);
        setChanged();
        notifyObservers(finishedEvent);
    }

    /**
     * State updated, some node made progress
     */
    public void updateState() {
    	
        for (ComputationNode node:nodes.values()) {
            if (node.existsUnfinishedTasks()) {
            	return;
            }
        }
        computationBatchFinished();
    }

    /**
     * Gets id of the graph
     * @return Id of the graph
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id of the graph
     * @param id Id of the graph
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets id of the batch
     * @return Batch id
     */
    public int getBatchID() {
        return batchID;
    }

    /**
     * Sets id of the batch
     * @param batchID Batch id
     */
    public void setBatchID(int batchID) {
        this.batchID = batchID;
    }
}
