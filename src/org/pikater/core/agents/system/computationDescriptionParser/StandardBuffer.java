package org.pikater.core.agents.system.computationDescriptionParser;

import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;

import java.util.LinkedList;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:07
 */
public class StandardBuffer<E> extends AbstractComputationBuffer<E> {
    LinkedList<E> buffer=new LinkedList<E>();
    private boolean data; 

    public StandardBuffer(ComputationNode source, ComputationNode target) {
           super(source,target);
    }

    @Override
    public boolean hasNext() {
        return buffer.size()>0;
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
}
