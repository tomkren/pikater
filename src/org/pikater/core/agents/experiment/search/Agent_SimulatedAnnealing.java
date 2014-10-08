package org.pikater.core.agents.experiment.search;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;
import org.pikater.core.options.search.SimulatedAnnealing_Box;

public class Agent_SimulatedAnnealing extends Agent_Search {
	/*
	 * Implementation of Simulated Annealing search
	 * Options:
	 * -E float
	 * minimum error rate (default 0.1)
	 * 
	 * -M int 
	 * maximal number of generations (default 10)
	 * 
	 * -T float
	 * Initial temperature (default 1.0)
	 * 
	 * -S float
	 * Stability of generation of new option - probability of keeping of option (default 0.5)
	 */
	private static final long serialVersionUID = -5087231723984887596L;

	private SearchSolution solution = null;
	private SearchSolution newSolution = null;
	private float evaluation = Float.MAX_VALUE;
	private double temperature = 0.0;
	private double stability = 0.5;
	private int numberOfTries = 0;
	private int maximumTries = 50;
	private double bestErrorRate = 1;
	private double finalErrorRate = 0.1;
	private boolean minimization = true;
	protected Random rndGen = new Random(1);

	@Override
	protected String getAgentType() {
		return "SimulatedAnnealing";
	}
	
	@Override
	protected AgentInfo getAgentInfo() {

		return SimulatedAnnealing_Box.get();
	}

	@Override
	protected boolean isFinished() {
		//n>=nmax
		if (numberOfTries >= maximumTries) {
			return true;
		}
		if (bestErrorRate <= finalErrorRate) {
			return true;
		}
		return false;
	}
	

	@Override
	protected void loadSearchOptions() {
		temperature = 1.0;
		maximumTries = 50;
		stability = 0.5;
		finalErrorRate = 0.01;
		
		NewOptions options = getSearchOptions();
		
		NewOption optionE = options.fetchOptionByName("E");
		FloatValue valueE =
				(FloatValue) optionE.toSingleValue().getCurrentValue();
		finalErrorRate = valueE.getValue(); 

		NewOption optionM = options.fetchOptionByName("M");
		IntegerValue valueM =
				(IntegerValue) optionM.toSingleValue().getCurrentValue();
		maximumTries = valueM.getValue(); 

		NewOption optionS = options.fetchOptionByName("S");
		FloatValue valueS =
				(FloatValue) optionS.toSingleValue().getCurrentValue();
		stability = valueS.getValue(); 

		NewOption optionT = options.fetchOptionByName("T");
		FloatValue valueT =
				(FloatValue) optionT.toSingleValue().getCurrentValue();
		temperature = valueT.getValue(); 
		
	}

	@Override
	protected List<SearchSolution> generateNewSolutions(
			List<SearchSolution> solutions, float[][] evaluations) {
		
		if(evaluations == null){
			// initialization
			solution = null;
			newSolution = null;
			evaluation = Float.MAX_VALUE;
			numberOfTries = 0;
			bestErrorRate = Double.MAX_VALUE;
		}
		
		// create a new solution for evaluation
		newSolution = neighbor(solution);
		
		numberOfTries++;
		
		// List of solutions to send
		List<SearchSolution> solutionsList =
				new ArrayList<SearchSolution>();
		solutionsList.add(newSolution);
		return solutionsList;
	}
	
	@Override
	protected float updateFinished(float[][] evaluations) {
		float newEvaluation;
		
		if (evaluations == null){
			newEvaluation = Float.MAX_VALUE;
		}
		else{
			newEvaluation = evaluations[0][0];
		}
		
		// Actualize best evaluation
		if(newEvaluation < bestErrorRate){
			bestErrorRate = newEvaluation;
		}
		// Acceptance of new solutions
		// System.out.print("<OK:> Temp:"+temperature+", e0: "+evaluation);
		
		double prob = acceptanceProb(newEvaluation-evaluation, temperature);
		if (rndGen.nextDouble() < prob){
			solution = newSolution;
			evaluation = newEvaluation;
		}
		
		/*
		System.out.println(", e1:" + newEvaluation + ", acceptance: " +
				acc + " ,.5->1: " + acceptanceProb(1-0.5,temperature) +
				" ,1->.5:"+ acceptanceProb(0.5-1,temperature));
		*/
		
		//Decrease temperature
		cooling();
		
		return (float) bestErrorRate;
	}
	
	/**
	 * Random solutions in case of beginning, or mutation of existing
	 * 
	 */
	private SearchSolution neighbor(SearchSolution solution) {

		List<IValueData> neighbourSolutionValues =
				new ArrayList<IValueData>();
		if(solution == null) {
			// Completely new solution
			for (SearchItem si : getSchema() ) {
				// don't want to change old solutions
				neighbourSolutionValues.add(si.randomValue(rndGen));
			}
		} else {
			// Neighbor function
			for (int i = 0; i < getSchema().size(); i++) {
				
				SearchItem si = getSchema().get(i);
                IValueData val = solution.getValues().get(i);
				
				if(rndGen.nextDouble() > stability) {
					val = si.randomValue(rndGen);
				}
				neighbourSolutionValues.add(val);
			}
		}
		
		SearchSolution result = new SearchSolution();
		result.setValues(neighbourSolutionValues);
		return result;
	}
	
	/**
	 * Acceptance probability of annealed solutions
	 *   -the better values are accepted
	 *   -the worse with probability exp((e-e_new)/temperature)
	 *   
	 */
	private double acceptanceProb(double delta, double temperature){
		
		// for max problems
		if (!minimization) {
			
			if (-delta < 0) {
				return 1.0;
			} else {
				return Math.exp(-delta/temperature);
			}
		
		} else {
			
			if (delta < 0) {
				return 1.0;
			} else {
				return Math.exp(-delta/temperature);
			}

		}
	}
	
	/**
	 * Cooling scheme: 20% decrease in each step
	 */
	private void cooling() {
		
		temperature = 0.8 * temperature;
	}

}
