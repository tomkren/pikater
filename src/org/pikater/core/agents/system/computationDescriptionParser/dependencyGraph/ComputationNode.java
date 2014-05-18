package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;
import org.pikater.core.agents.system.computationDescriptionParser.edges.EdgeValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:35
 */
public class ComputationNode {
    private boolean idle;
    private int id;
    private Map<String, ArrayList<ComputationOutputBuffer<EdgeValue>>> outputs = new HashMap<String, ArrayList<ComputationOutputBuffer<EdgeValue>>>();
    private Map<String, ComputationOutputBuffer> inputs = new HashMap<String, ComputationOutputBuffer>();
    private StartComputationBehavior startBehavior;

    public ComputationNode()
    {
        String initBeansName = "Beans.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(initBeansName);
        GUIDGenerator generator= (GUIDGenerator) context.getBean("guidGenerator");
        id=generator.getAndAllocateGUID();
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
        for (ComputationOutputBuffer input:inputs.values())
        {
            if (input.size()==0 && !input.isBlocked())
            {
                return false;
            }
        }
        return true;
    }

    public void addInput(String inputName, ComputationOutputBuffer buffer)
    {
        inputs.put(inputName,buffer);
    }

    public void addOutput(String outputName)
    {
    	ArrayList<ComputationOutputBuffer<EdgeValue>> emptyList = new ArrayList<ComputationOutputBuffer<EdgeValue>>();
        outputs.put(outputName, emptyList);
    }

    public void addBufferToOutput(String outputName,ComputationOutputBuffer buffer)
    {
        addOutput(outputName);
        outputs.get(outputName).add(buffer);
    }

    public void addToOutputAndProcess(EdgeValue o, String outputName)
    {
        ArrayList<ComputationOutputBuffer<EdgeValue>> outs=outputs.get(outputName);
        for (ComputationOutputBuffer<EdgeValue> out:outs)
        {
            out.insert(o);
            if (out.size()==1)
            {
                //was zero before - check for computation start
                if (out.getTarget().canComputationStart())
                {
                    out.getTarget().startComputation();
                }
            }
        }
    }

    public void startComputation()
    {
        startBehavior.execute(this);
    }

    public void computationFinished()
    {
        idle=true;
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

    public StartComputationBehavior getStartBehavior() {
        return startBehavior;
    }

    public void setStartBehavior(StartComputationBehavior startBehavior) {
        this.startBehavior = startBehavior;
    }
}
