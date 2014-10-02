package org.pikater.core.agents.system.computation.parser;

/**
 * Type of buffer with one element that always replenish itself
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:05
 * Implements a buffer that has single item
 */
public class NeverEndingBuffer<E> extends AbstractComputationBuffer<E>
{
    private E singleElement;
    private boolean data; 
    private String targetInput;

    public NeverEndingBuffer()
    {
    }

    /**
     *
     * @param singleElement Replenishing element
     */
	public NeverEndingBuffer(E singleElement)
    {
        this.singleElement=singleElement;
    }
    

    @Override
    public boolean hasNext() {
        return singleElement!=null;
    }

    /**
     * Sets new replenishing element
     * @param element Element to insert
     */
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
	public void setTargetInput(String targetInput) {
		this.targetInput = targetInput;		
	}	
	@Override
	public String getTargetInput() {
		return targetInput;
	}	
}
