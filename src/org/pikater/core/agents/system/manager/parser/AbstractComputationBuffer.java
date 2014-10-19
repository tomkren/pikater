package org.pikater.core.agents.system.manager.parser;

import org.pikater.core.agents.system.manager.graph.ComputationNode;

/**
 * Abstract class implementing common methods of buffer interface
 * User: Kuba
 * Date: 10.5.2014
 * Time: 12:46
 */
public abstract class AbstractComputationBuffer<E>implements
		ComputationOutputBuffer<E> {
	
    private ComputationNode source;
    private ComputationNode target;
    private boolean isBlocked = false;

    /**
     * Constructor
     * 
     * @param source Source node
     * @param target Target node
     */
    public AbstractComputationBuffer(ComputationNode source,
    		ComputationNode target) {
    	
        setSource(source);
        setTarget(target);
    }

    /**
     * Constructor
     * 
     * @param target Target node
     */
    public AbstractComputationBuffer(ComputationNode target) {
        setTarget(target);
    }

    /**
     * Constructor
     */
    public AbstractComputationBuffer() {
    }

    @Override
    public ComputationNode getSource() {
        return source;
    }

    @Override
    public void setSource(ComputationNode source) {
        this.source = source;
    }

    @Override
    public ComputationNode getTarget() {
        return target;
    }

    @Override
    public void setTarget(ComputationNode target) {
        this.target = target;
    }

    @Override
    public boolean isBlocked() {
        return isBlocked;
    }

    @Override
    public void block() {
        isBlocked = true;
    }

    @Override
    public void unblock() {
        isBlocked = false;
    }

}
