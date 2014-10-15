package org.pikater.core.agents.system.computation.parser;

import org.pikater.core.agents.system.computation.graph.ComputationNode;

/**
 * Interface for queues in computation graph
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:03
 */
public interface ComputationOutputBuffer<E> {
    /**
     * Has queue at least one element
     * @return True if has next element
     */
    public boolean hasNext();

    /**
     * Insert to queue
     * @param element Element to insert
     */
    public void insert(E element);

    /**
     * Get next element
     * @return Next element
     */
    public E getNext();

    /**
     * Size of the queue
     * @return Elements in queue
     */
    public int size();

    /**
     * Gets source node of the queue (will insert elements)
     * @return Source of the queue
     */
    public ComputationNode getSource();

    /**
     * Sets source node
     * @param source Source node
     */
    public void setSource(ComputationNode source);

    /**
     * Gets target node (will consume elements)
     * @return Target node
     */
    public ComputationNode getTarget();
    /**
     * Sets target node (will consume elements)
     */
    public void setTarget(ComputationNode target);

    /**
     * Gets name of target input
     * @return Name of target input
     */
    public String getTargetInput();

    /**
     * Gets if queue is blocked - will not be registered by target - one hsot options etc.
     * @return  True if blocked
     */
    public boolean isBlocked();

    /**
     * Sets blocked to true
     */
    public void block();

    /**
     * Sets block to false
     */
    public void unblock();

    /**
     * Gets isdata flag
     * @return True if isdata
     */
    public boolean isData();

    /**
     * Sets isData flag
     * @param data IsData flag
     */
    public void setData(boolean data);
}
