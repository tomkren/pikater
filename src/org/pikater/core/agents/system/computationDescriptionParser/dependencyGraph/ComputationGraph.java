package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import java.util.HashMap;

/**
 * User: Kuba
 * Date: 11.5.2014
 * Time: 21:27
 */
public class ComputationGraph {
    private HashMap<Integer,ComputationNode> nodes=new HashMap<>();

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
    public void startComputation()
    {
           for (ComputationNode node:nodes.values())
           {
               if (node.canComputationStart())
               {
                   node.startComputation();
               }
           }
    }
}
