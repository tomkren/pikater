package org.pikater.core.agents.system.computationDescriptionParser;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:05
 * Implements a buffer that has single item
 */
public class NeverEndingBuffer<E> extends AbstractComputationBuffer<E> {
    private E singleElement;

    public NeverEndingBuffer(E singleElement)
    {
        this.singleElement=singleElement;
    }

    @Override
    public boolean hasNext() {
        return true;
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
        return 1;
    }
}
