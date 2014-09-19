package org.pikater.core.agents.system.computation.planning.structures;

import java.io.Serializable;
import java.util.Comparator;

public class PlannerComparator implements Comparator<TaskToSolve>, Serializable
{
	private static final long serialVersionUID = -8855770050181866067L;

	@Override
	public int compare(TaskToSolve task0, TaskToSolve task1)
	{
		
		int priority0 = task0.getPriority();
		int priority1 = task1.getPriority();
		
		return priority0 - priority1;
	}
}