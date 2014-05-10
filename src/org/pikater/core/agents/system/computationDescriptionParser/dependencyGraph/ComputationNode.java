package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import org.pikater.core.agents.system.computationDescriptionParser.ComputationOutputBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:35
 */
public class ComputationNode {
    private boolean idle;
    private int id;
    private ArrayList<ComputationOutputBuffer> outputs;
    private ArrayList<ComputationOutputBuffer> inputs;

    public boolean canComputationStart()
    {
        for (ComputationOutputBuffer input:inputs)
        {
            if (input.size()==0)
            {
                return false;
            }
        }
        return true;
    }

    /*
    TODO:observer pattern maybe
     */
    public void ComputationFinished()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
