package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import java.util.HashMap;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: Kuba
 * Date: 11.5.2014
 * Time: 21:27
 */
public class ComputationGraph {
    private HashMap<Integer,ComputationNode> nodes=new HashMap<Integer,ComputationNode>();

    private int id;
    
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

    public void computationBatchFinished(PikaterAgent agent, int batchID)
    {
    	DataManagerService.updateBatchStatus(
    			agent, batchID, JPABatchStatus.FINISHED.name());
        //TODOJakub: call this when the batch is completed
    }

    public void computationExperimentFinished(PikaterAgent agent, int experimentID)
    {
    	DataManagerService.updateExperimentStatus(
    			agent, experimentID, JPAExperimentStatus.FINISHED.name());
        //TODOJakub: call this when the experiment is completed
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
