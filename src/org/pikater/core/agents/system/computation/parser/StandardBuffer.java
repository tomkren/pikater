package org.pikater.core.agents.system.computation.parser;

import org.pikater.core.agents.system.computation.graph.ComputationNode;

import java.util.LinkedList;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:07
 */
public class StandardBuffer<E> extends AbstractComputationBuffer<E> {
    LinkedList<E> buffer=new LinkedList<E>();
    private boolean data; 
    private String targetInput;

    public StandardBuffer(ComputationNode source, ComputationNode target) {
           super(source,target);
    }

    @Override
    public boolean hasNext() {
        return !buffer.isEmpty();
    }

    @Override
    public void insert(E element) {
         buffer.addLast(element);
    }

    @Override
    public E getNext() {
        return buffer.pollFirst();
    }

    @Override
    public int size() {
        return buffer.size();
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
