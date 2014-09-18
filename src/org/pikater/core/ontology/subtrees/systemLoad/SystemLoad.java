package org.pikater.core.ontology.subtrees.systemLoad;

import java.util.logging.Level;

import org.pikater.shared.logging.core.ConsoleLogger;

import jade.content.Concept;

public class SystemLoad implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7779159815343198241L;

	private int numberOfTasksInQueue;
	private int numberOfBusyCores;
	private int numberOfFreeCores;
	
	public int getNumberOfTasksInQueue() {
		return numberOfTasksInQueue;
	}
	public void setNumberOfTasksInQueue(int numberOfTasksInQueue) {
		this.numberOfTasksInQueue = numberOfTasksInQueue;
	}
	
	public int getNumberOfBussyCores() {
		return numberOfBusyCores;
	}
	public void setNumberOfBusyCores(int numberOfBussyCores) {
		this.numberOfBusyCores = numberOfBussyCores;
	}
	
	public int getNumberOfFreeCores() {
		return numberOfFreeCores;
	}
	public void setNumberOfUntappedCores(int numberOfFreeCores) {
		this.numberOfFreeCores = numberOfFreeCores;
	}
	
	public void print()
	{
		StringBuilder sb = new StringBuilder("Planner:\n");
		sb.append("\tNumberOfTasksInQueue: " + String.valueOf(numberOfTasksInQueue));
		sb.append("\tNumberOfBussyCores: " + String.valueOf(numberOfBusyCores));
		sb.append("\tNumberOfFreeCores: " + String.valueOf(numberOfFreeCores));
		ConsoleLogger.log(Level.INFO, sb.toString());
	}
}