package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: Kuba
 * Date: 11.5.2014
 * Time: 21:27
 */
public class ComputationGraph {
    private HashMap<Integer,ComputationNode> nodes=new HashMap<>();

    private int id;
    
    public ComputationGraph()
    {
        String initBeansName = "Beans.xml";
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
        
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
