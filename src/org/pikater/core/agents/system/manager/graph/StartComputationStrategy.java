package org.pikater.core.agents.system.manager.graph;

/**
 * Strategy design pattern, used in computation graph - when all inputs are
 * filled, the execute method is triggered User: Kuba Date: 10.5.2014 Time:
 * 14:12
 */
public interface StartComputationStrategy {
	/**
	 * Executes the strategz - get inputs, compute and then fill outputs or add
	 * JADE behavior that will do it later when the computation is finished
	 * 
	 * @param computation
	 *            Computation node with this strategy
	 */
	public void execute(ComputationNode computation);
}
