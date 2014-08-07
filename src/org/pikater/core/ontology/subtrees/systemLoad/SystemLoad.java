package org.pikater.core.ontology.subtrees.systemLoad;

import jade.content.Concept;

public class SystemLoad implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7779159815343198241L;

	private int numberOfTasksInQueue;
	
	private int numberOfBussyCores;
	private int numberOfFreeCores;
	
	public int getNumberOfTasksInQueue() {
		return numberOfTasksInQueue;
	}
	public void setNumberOfTasksInQueue(int numberOfTasksInQueue) {
		this.numberOfTasksInQueue = numberOfTasksInQueue;
	}
	
	public int getNumberOfBussyCores() {
		return numberOfBussyCores;
	}
	public void setNumberOfBusyCores(int numberOfBussyCores) {
		this.numberOfBussyCores = numberOfBussyCores;
	}
	
	public int getNumberOfFreeCores() {
		return numberOfFreeCores;
	}
	public void setNumberOfUntappedCores(int numberOfFreeCores) {
		this.numberOfFreeCores = numberOfFreeCores;
	}
	
	public void print() {
		
		System.out.println("Planner:");
		System.out.println("NumberOfTasksInQueue: " + numberOfTasksInQueue);
		System.out.println("NumberOfBussyCores:   " + numberOfBussyCores);
		System.out.println("NumberOfFreeCores:    " + numberOfFreeCores);
	}
	
}
