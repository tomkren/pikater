package org.pikater.core.agents.system.computation.planning.structures;

import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration.DurationType;

import org.pikater.core.ontology.subtrees.task.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class WaitingTasksQueues {
	
	private Queue<TaskToSolve> shortTimeDurationQueue;
	private Queue<TaskToSolve> longTermDurationQueue;
	
	public WaitingTasksQueues() {
		
		Comparator<TaskToSolve> comparator = new PlannerComparator();
		
		shortTimeDurationQueue = new PriorityQueue<TaskToSolve>(10, comparator);
		longTermDurationQueue = new PriorityQueue<TaskToSolve>(10, comparator);
	}
	
	public void addTasks(Set<TaskToSolve> taskToSolve) {
		List<TaskToSolve> tasks = new ArrayList<TaskToSolve>(taskToSolve);
		
		for (TaskToSolve taskToSolveI : tasks) {
			addTask(taskToSolveI);
		}
	}
	
	public void addTask(TaskToSolve taskToSolve) {
		
		ExpectedDuration expectedDuration =
				taskToSolve.getTask().getExpectedDuration();
		DurationType durationType = expectedDuration.exportDurationType();
		
		if (durationType.equals(ExpectedDuration.DurationType.SECONDS)) {
		
			this.shortTimeDurationQueue.add(taskToSolve);
		} else {
			
			this.longTermDurationQueue.add(taskToSolve);
		}
	}
	
	public void updateTaskPriority(int batchID, int newPriority) {

		Comparator<TaskToSolve> comparator = new PlannerComparator();

		PriorityQueue<TaskToSolve> newShortTimeDurationQueue =
				new PriorityQueue<TaskToSolve>(10, comparator);
		while (!shortTimeDurationQueue.isEmpty()) {
			TaskToSolve taskToSolveI = shortTimeDurationQueue.remove();
			Task taskI = taskToSolveI.getTask();

			if (taskI.getBatchID() == batchID) {
				taskToSolveI.setPriority(newPriority);
			}
			newShortTimeDurationQueue.add(taskToSolveI);
		}
		shortTimeDurationQueue = newShortTimeDurationQueue;

		PriorityQueue<TaskToSolve> newLongTermDurationQueue =
				new PriorityQueue<TaskToSolve>(10, comparator);
		while (!longTermDurationQueue.isEmpty()) {
			TaskToSolve taskToSolveI = longTermDurationQueue.remove();
			Task taskI = taskToSolveI.getTask();

			if (taskI.getBatchID() == batchID) {
				taskToSolveI.setPriority(newPriority);
			}
			newLongTermDurationQueue.add(taskToSolveI);
		}
		longTermDurationQueue = newLongTermDurationQueue;
	}
	
	public TaskToSolve removeTaskWithHighestPriority() {
		
		if (this.shortTimeDurationQueue.size() != 0) {
			return this.shortTimeDurationQueue.remove();
			
		} else if (this.longTermDurationQueue.size() != 0) {
			return this.longTermDurationQueue.remove();
		}
		
		return null;
	}

	public boolean isExistingTaskToSolve() {
		
		return getNumberOfTasksInQueue() > 0;
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
