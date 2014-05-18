package org.pikater.core.agents.system.computationDescriptionParser;

import org.pikater.core.agents.system.computationDescriptionParser.edges.EdgeValue;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 12:44
 */
public class OneShotBuffer extends AbstractComputationBuffer<EdgeValue> {
    private EdgeValue shot;

    public OneShotBuffer(EdgeValue shot)
    {
         this.shot=shot;
    }

    @Override
    public boolean hasNext() {
        return shot!=null;
    }

    @Override
    public void insert(EdgeValue element) {
        shot=element;
    }

    @Override
    public EdgeValue getNext() {
        EdgeValue current=shot;
        shot=null;
        return current;
    }

    @Override
    public int size() {
        if (shot!=null)
        {
            return 1;
        }
        return 0;
    }
}
