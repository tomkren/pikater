package org.pikater.core.agents.system.planner.dataStructures;

import java.util.Comparator;

public class PlannerComparator implements Comparator<TaskToSolve> {

	@Override
	public int compare(TaskToSolve task0, TaskToSolve task1) {
		
		int priority0 = task0.getTask().getPriority();
		int priority1 = task1.getTask().getPriority();
		
		return priority0 - priority1;
	}

}
