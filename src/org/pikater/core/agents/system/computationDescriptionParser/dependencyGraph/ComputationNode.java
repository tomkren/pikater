package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;
import org.pikater.core.agents.system.computationDescriptionParser.edges.EdgeValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:35
 */
public class ComputationNode {
    private boolean idle;
    private int id;
    private HashMap<String,ArrayList<ComputationOutputBuffer<EdgeValue>>> outputs;
    private HashMap<String,ComputationOutputBuffer> inputs;
    private StartComputationBehavior startBehavior;

    public boolean canComputationStart()
    {
        if (!idle)
        {
            //another computation is running
            return false;
        }
        for (ComputationOutputBuffer input:inputs.values())
        {
            if (input.size()==0)
            {
                return false;
            }
        }
        return true;
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
