package org.pikater.core.agents.system.computation.parser;

import org.pikater.core.agents.system.computation.graph.ComputationNode;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:03
 */
public interface ComputationOutputBuffer<E> {
    public boolean hasNext();
    public void insert(E element);
    public E getNext();
    public int size();
    public boolean isEmpty();
    public ComputationNode getSource();
    public void setSource(ComputationNode source);
    public ComputationNode getTarget();
    public void setTarget(ComputationNode target);
    public void setTargetInput(String targetInput);
    public String getTargetInput();
    public boolean isBlocked();
    public void block();
    public void unblock();
    public boolean isData();
    public void setData(boolean data);
}
