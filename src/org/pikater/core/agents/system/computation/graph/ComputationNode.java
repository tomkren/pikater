package org.pikater.core.agents.system.computation.graph;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.system.computation.graph.edges.EdgeValue;
import org.pikater.core.agents.system.computation.parser.ComputationOutputBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Computation node stores inputs and outputs for some task in computation graph (recommend, search, compute]. It laso contains appropriate strategy determined by the node type
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:35
 */
public class ComputationNode {
    private boolean idle=true;
    private int id;
    protected int numberOfTasksInProgress;
    private Map<String, ArrayList<ComputationOutputBuffer<EdgeValue>>> outputs = new HashMap<>();
    private Map<String, ComputationOutputBuffer> inputs = new HashMap<>();
    private StartComputationStrategy startBehavior;
    protected ComputationGraph computationGraph;

    /**
     *
     * @param computationGraph Owning computation graph
     */
    public ComputationNode(ComputationGraph computationGraph)
    {
        this.computationGraph = computationGraph;
        initDefault();
    }

    /**
     *
     * @param executeStrategy Strategy that will be executed if the inputs are filled
     */
    public ComputationNode(StartComputationStrategy executeStrategy)
    {
        initDefault();
        startBehavior=executeStrategy;
    }

    /**
     * Contains output with some name
     * @param outputName Name of the output
     * @return True if present
     */
    public Boolean containsOutput(String outputName)
    {
        return outputs.containsKey(outputName);
    }

    /**
     * Gte all inputs
     * @return All inputs defined in this node
     */
    public Map<String,ComputationOutputBuffer> getInputs()
    {
         return inputs;
    }

    /**
     * Can computation starts - inputs filled, is not blocked, etc.
     * @return True if all prerequisities satisifed
     */
    public boolean canComputationStart()
    {
        if (!idle)
        {
            //another computation is running
            return false;
        }
        for (ComputationOutputBuffer input : inputs.values())
        {
            if (input.size()==0 && !input.isBlocked())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Add input to inputs
     * @param inputName name of the new inputs
     * @param buffer Buffer of the input
     */
    public void addInput(String inputName,ComputationOutputBuffer buffer)
    {        
        if (! inputs.containsKey(inputName) ) {
        	inputs.put(inputName,buffer);
        }
    }

    /**
     * Add ouputt to outputs
     * @param outputName Name of the new output
     */
    public void addOutput(String outputName)
    {   
        if (! outputs.containsKey(outputName) ) {
        	outputs.put(outputName, new ArrayList<ComputationOutputBuffer<EdgeValue>>());
        }

    }

    /**
     * Add buffer to named output
     * @param outputName name of the output receiving the buffer
     * @param buffer Buffer to add
     */
    public void addBufferToOutput(String outputName,ComputationOutputBuffer buffer)
    {
        addOutput(outputName);
        outputs.get(outputName).add(buffer);
    }


    /**
     * Add edge value to output and check if the computation can go to the successors
     * @param o New edge value that will be added to the output
     * @param outputName Name of the output
     */
    public void addToOutputAndProcess(EdgeValue o, String outputName)
    {
    	addToOutputAndProcess(o, outputName, false, false);
    }

    /**
     * Add edge value to output and check if the computation can go to the successors
     * @param o New edge value that will be added to the output
     * @param outputName Name of the output
     * @param unblock If output should be unblocked
     * @param isData Mark as data
     */
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

    /**
     * Starts computation - will be busy and execute appropriate behavior
     */
    public void startComputation()
    {
        idle=false;
        startBehavior.execute(this);
    }

    /**
     * Computation finished, not busy, will check if computation can start and execute if possible
     */
    public void computationFinished()
    {
        idle=true;
        numberOfTasksChanged();
        if (canComputationStart())
        {
            startComputation();
        }
    }

    /**
     * Gets id of this node
     * @return Id of this node
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id of this node
     * @param id Id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets startegz that will be executed on computation start
     * @param startBehavior
     */
    public void setStartBehavior(StartComputationStrategy startBehavior) {
        this.startBehavior = startBehavior;
    }

    private void initDefault()
    {
        id = CoreConfiguration.getGUIDGenerator().getAndAllocateGUID();
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
    	List<String> keys = new ArrayList<>();
    	
    	Iterator it = outputs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ArrayList<ComputationOutputBuffer<EdgeValue>>> pairs = (Map.Entry)it.next();
        	Iterator it1 = pairs.getValue().iterator();
            while (it1.hasNext()) {
            	ComputationOutputBuffer<EdgeValue> cob = (ComputationOutputBuffer<EdgeValue>)it1.next();
                String target = cob.getTargetInput();
                if (target != null && target.equals(in)){ 
                	keys.add(pairs.getKey());            	
                }
            }            
        }
    	return keys; 
    }
}
