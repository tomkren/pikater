package org.pikater.core.ontology.subtrees.result;

import org.pikater.core.ontology.subtrees.task.Id;

import jade.content.Concept;
import jade.util.leap.List;

public class Results implements Concept {

	/**
	 * 
	 */	
	private static final long serialVersionUID = -3411423409276645995L;
	
	private float maxValue = (float)Integer.MAX_VALUE;
	
	private String problemId;
	private Id taskId;

	private float avgErrorRate = -1;
	private float avgKappaStatistic = -1;
	private float avgMeanAbsoluteError = -1;
	private float avgRootMeanSquaredError = -1;
	private float avgRelativeAbsoluteError = -1;
	private float avgRootRelativeSquaredError = -1;

	private List results;
	
	public String getProblemId() {
		return problemId;
	}
	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}

	public Id getTaskId() {
		return taskId;
	}
	public void setTaskId(Id taskId) {
		this.taskId = taskId;
	}

	public List getResults() {
		return results;
	}
	public void setResults(List results) {
		this.results = results;
	}

	public float getAvgErrorRate() {
		return avgErrorRate;
	}
	public void setAvgErrorRate(float avgErrorRate) {
		if (Float.isInfinite(avgErrorRate)){
			this.avgErrorRate = maxValue;
		}
		else{
			this.avgErrorRate = avgErrorRate;
		}
	}

	public float getAvgKappaStatistic() {
		return avgKappaStatistic;
	}
	public void setAvg_kappa_statistic(float avgKappaStatistic) {
		if (Float.isInfinite(avgKappaStatistic)){
			this.avgKappaStatistic = maxValue;
		}
		else{
			this.avgKappaStatistic = avgKappaStatistic;
		}
	}

	public float getAvgMeanAbsoluteError() {
		return avgMeanAbsoluteError;
	}
	public void setAvgMeanAbsoluteError(float avgMeanAbsoluteError) {
		if (Float.isInfinite(avgMeanAbsoluteError)){
			this.avgMeanAbsoluteError = maxValue;
		}
		else{		
			this.avgMeanAbsoluteError = avgMeanAbsoluteError;
		}
	}

	public float getAvgRootMeanSquaredError() {
		return avgRootMeanSquaredError;
	}
	public void setAvg_root_mean_squared_error(float avgRootMeanSquaredError) {
		if (Float.isInfinite(avgRootMeanSquaredError)){
			this.avgRootMeanSquaredError = maxValue;
		}
		else{
			this.avgRootMeanSquaredError = avgRootMeanSquaredError;
		}
	}

	public float getAvgRelativeAbsoluteError() {
		return avgRelativeAbsoluteError;
	}
	public void setAvgRelativeAbsoluteError(float avgRelativeAbsoluteError) {
		if (Float.isInfinite(avgRelativeAbsoluteError)){
			this.avgRelativeAbsoluteError = maxValue;
		}
		else{
			this.avgRelativeAbsoluteError = avgRelativeAbsoluteError;
		}
	}

	public float getAvgRootRelativeSquaredError() {
		return avgRootRelativeSquaredError;
	}
	public void setAvgRootRelativeSquaredError(
			float avgRootRelativeSquaredError) {
		if (Float.isInfinite(avgRootRelativeSquaredError)){
			this.avgRootRelativeSquaredError = maxValue;
		}
		else{		
			this.avgRootRelativeSquaredError = avgRootRelativeSquaredError;
		}
	}
	
}