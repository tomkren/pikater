package org.pikater.core.agents.system.computationDescriptionParser;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:05
 * Implements a buffer that has single item
 */
public class NeverEndingBuffer<E> extends AbstractComputationBuffer<E> {
    private E singleElement;
    private boolean data; 
    private String targetInput;

	public NeverEndingBuffer(E singleElement)
    {
        this.singleElement=singleElement;
    }
    public NeverEndingBuffer(){};

    @Override
    public boolean hasNext() {
        return singleElement!=null;
    }

    public void insert(E element) {
           singleElement=element;
    }

    @Override
    public E getNext() {
        return singleElement;
    }

    @Override
    public int size() {
        return singleElement==null? 0: 1;
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
