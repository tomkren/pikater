package org.pikater.core.agents.system.planner.dataStructures;

import org.pikater.core.ontology.subtrees.batchDescription.durarion.IExpectedDuration;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.LongTermDuration;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ShortTimeDuration;
import org.pikater.core.ontology.subtrees.task.Task;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class WaitingTasksQueues {
	
	private Queue<TaskToSolve> shortTimeDurationQueue;
	private Queue<TaskToSolve> longTermDurationQueue;
	
	public WaitingTasksQueues() {
		
		Comparator<TaskToSolve> comparator = new PlannerComparator();
		
		shortTimeDurationQueue = new PriorityQueue<TaskToSolve>(10, comparator);
		longTermDurationQueue = new PriorityQueue<TaskToSolve>(10, comparator);
	}
	
	public void addTask(TaskToSolve taskToSolve) {
		
		IExpectedDuration expectedDuration =
				taskToSolve.getTask().getExpectedDuration();
		if (expectedDuration instanceof ShortTimeDuration) {
		
			this.shortTimeDurationQueue.add(taskToSolve);
		} else if (expectedDuration instanceof LongTermDuration) {
			
			this.longTermDurationQueue.add(taskToSolve);
		} else {
			throw new IllegalArgumentException("Illegal field expectedDuration");
		}
	}
	
	public TaskToSolve removeTaskWithHighestPriority() {
		
		if (this.shortTimeDurationQueue.size() != 0) {
			return this.shortTimeDurationQueue.remove();
			
		} else if (this.longTermDurationQueue.size() != 0) {
			return this.longTermDurationQueue.remove();
		}
		
		return null;
	}

	public void removeTasks(int batchID) {
		
		Comparator<TaskToSolve> comparator = new PlannerComparator();

		PriorityQueue<TaskToSolve> newShortTimeDurationQueue =
				 new PriorityQueue<TaskToSolve>(10, comparator);
        while (!shortTimeDurationQueue.isEmpty()) {
        	TaskToSolve taskToSolveI = shortTimeDurationQueue.remove();
        	Task taskI = taskToSolveI.getTask();
        	
        	if (taskI.getBatchID() != batchID) {
        		newShortTimeDurationQueue.add(taskToSolveI);
        	}
        }
		shortTimeDurationQueue = newShortTimeDurationQueue;
		
		PriorityQueue<TaskToSolve> newLongTermDurationQueue =
				 new PriorityQueue<TaskToSolve>(10, comparator);
        while (!longTermDurationQueue.isEmpty()) {
       	TaskToSolve taskToSolveI = longTermDurationQueue.remove();
       	Task taskI = taskToSolveI.getTask();
       	
	       	if (taskI.getBatchID() != batchID) {
	       		newLongTermDurationQueue.add(taskToSolveI);
	       	}
       }
       longTermDurationQueue = newLongTermDurationQueue;
	}
	
	public int getNumberOfTasksInQueue() {
		return shortTimeDurationQueue.size() +
				longTermDurationQueue.size();
	}
	
}
