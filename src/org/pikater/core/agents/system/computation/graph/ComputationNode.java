package org.pikater.core.agents.system.computation.graph;

import org.pikater.core.agents.system.computation.graph.edges.EdgeValue;
import org.pikater.core.agents.system.computation.parser.ComputationOutputBuffer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:35
 */
public class ComputationNode {
    private boolean idle=true;
    private int id;
    protected int numberOfTasksInProgress;
    private Map<String, ArrayList<ComputationOutputBuffer<EdgeValue>>> outputs = new HashMap<String, ArrayList<ComputationOutputBuffer<EdgeValue>>>();
    private Map<String, ComputationOutputBuffer> inputs = new HashMap<String, ComputationOutputBuffer>();
    private StartComputationStrategy startBehavior;
    protected ComputationGraph computationGraph;

    public ComputationNode(ComputationGraph computationGraph)
    {
        this.computationGraph = computationGraph;
        initDefault();
    }

    public ComputationNode(StartComputationStrategy executeStrategy,ComputationGraph computationGraph)
    {
        initDefault();
        startBehavior=executeStrategy;
    }

    public Boolean containsOutput(String outputName)
    {
        return outputs.containsKey(outputName);
    }

    public Map<String,ComputationOutputBuffer> getInputs()
    {
         return inputs;
    }

    public boolean canComputationStart()
    {
        if (!idle)
        {
            //another computation is running
            return false;
        }
        for (ComputationOutputBuffer input : inputs.values())
        {
            if (input.isEmpty() && !input.isBlocked())
            {
                return false;
            }
        }
        return true;
    }

    public void addInput(String inputName,ComputationOutputBuffer buffer)
    {        
        if (! inputs.containsKey(inputName) ) {
        	inputs.put(inputName,buffer);
        }
    }

    public void addOutput(String outputName)
    {   
        if (! outputs.containsKey(outputName) ) {
        	outputs.put(outputName, new ArrayList<ComputationOutputBuffer<EdgeValue>>());
        }

    }

    public void addBufferToOutput(String outputName,ComputationOutputBuffer buffer)
    {
        addOutput(outputName);
        outputs.get(outputName).add(buffer);
    }

    
    public void addToOutputAndProcess(EdgeValue o, String outputName)
    {
    	addToOutputAndProcess(o, outputName, false, false);
    }
    
    public void addToOutputAndProcess(EdgeValue o, String outputName, Boolean unblock, Boolean isData)
    {
    	if (outputs.get(outputName)==null){
    		return;
    	}
        List<ComputationOutputBuffer<EdgeValue>> outs = outputs.get(outputName);
        for (ComputationOutputBuffer<EdgeValue> out:outs)
        {
        	if (unblock)
        	{
        		out.unblock();
        	}

        	out.setData(isData);
        	
        	out.insert(o);
            if ((out.size() == 1) && out.getTarget().canComputationStart()) // was zero before - check for computation start
            {
            	out.getTarget().startComputation();
            }
        }
    }

    public void startComputation()
    {
        idle=false;
        startBehavior.execute(this);
    }

    public void computationFinished()
    {
        idle=true;
        numberOfTasksChanged();
        if (canComputationStart())
        {
            startComputation();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public StartComputationStrategy getStartBehavior() {
        return startBehavior;
    }

    public void setStartBehavior(StartComputationStrategy startBehavior) {
        this.startBehavior = startBehavior;
    }

    private void initDefault()
    {
        String initBeansName = "Beans.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(initBeansName);
        GUIDGenerator generator= (GUIDGenerator) context.getBean("guidGenerator");
        id=generator.getAndAllocateGUID();
    }

    public int getNumberOfTasksInProgress() {
        return numberOfTasksInProgress;
    }

    public void setNumberOfTasksInProgress(int numberOfTasksInProgress) {
        this.numberOfTasksInProgress = numberOfTasksInProgress;
        numberOfTasksChanged();
    }

    public void numberOfTasksChanged()
    {
        if (numberOfTasksInProgress==0)
        {
            computationGraph.updateState();
        }
    }

    public void decreaseNumberOfOutstandingTask()
    {
        numberOfTasksInProgress--;
        numberOfTasksChanged();
    }

    public void increaseNumberOfOutstandingTask()
    {
        numberOfTasksInProgress++;
        numberOfTasksChanged();
    }

    public boolean existsUnfinishedTasks()
    {
        return numberOfTasksInProgress > 0 || !idle;
    }
    
    public List<String> findOutput(String in){
    	List<String> keys = new ArrayList<String>();
    	
    	Iterator it = outputs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ArrayList<ComputationOutputBuffer<EdgeValue>>> pairs = (Map.Entry)it.next();
        	Iterator it1 = pairs.getValue().iterator();
            while (it1.hasNext()) {
            	ComputationOutputBuffer<EdgeValue> cob = (ComputationOutputBuffer<EdgeValue>)it1.next();
                String target = (String)cob.getTargetInput();
                if (target != null && target.equals(in)){ 
                	keys.add(pairs.getKey());            	
                }
            }            
        }
    	return keys; 
    }
}
