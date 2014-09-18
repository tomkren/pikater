package org.pikater.core.agents.system.computation.parser;

import org.pikater.core.agents.system.computation.graph.edges.EdgeValue;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 12:44
 */
public class OneShotBuffer extends AbstractComputationBuffer<EdgeValue> {
    private EdgeValue shot;
    private boolean data; 
    private String targetInput;
    
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
    
	@Override
	public boolean isData() {
		return data;
	}

	@Override
	public void setData(boolean data) {
		this.data = data;
	}
	
	@Override
	public void setTargetInput(String targetInput) {
		this.targetInput = targetInput;		
	}	
	@Override
	public String getTargetInput() {
		return targetInput;
	}
}
