package org.pikater.core.agents.system.computationDescriptionParser;

import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;

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
    public ComputationNode getSource();
    public void setSource(ComputationNode source);
    public ComputationNode getTarget();
    public void setTarget(ComputationNode target);
    public boolean isBlocked();
    public void block();
    public void unblock();
    public boolean isData();
    public void setData(boolean data);
}
