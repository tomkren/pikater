package org.pikater.core.agents.experiment.search;


import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newoption.NewOptions;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchitems.SearchItem;
import org.pikater.core.options.search.GASearch_Box;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of Genetic algorithm search.
 * Half uniform crossover, tournament selection.
 * <p>
 * Options:
 * <ul>
 *  <li>-E float    Minimum error rate (default 0.1)
 *  <li>-M int      Maximum number of generations (default 10)
 *  <li>-T float    Mutation rate (default 0.2)
 *  <li>-X float    Crossover probability (default 0.5)
 *  <li>-P int      Population size (default 5)
 *  <li>-S int      Size of tournament in selection (default 2)
 * </ul> 
 */
public class Agent_GASearch extends Agent_Search {
	
	private ArrayList<SearchSolution> population;
	//fitness is the error rate - the lower, the better!
	float[] fitnesses;
	int popSize = 0;
	double mutProb = 0.0;
	double xoverProb = 0.0;
	private int numberOfGenerations = 0;
	private double bestErrorRate = Double.MAX_VALUE;
	
	private int maximumGenerations;
	private double finalErrorRate;
	int tournamentSize = 2;
	protected Random rndGen = new Random(1);

	private static final long serialVersionUID = -387458001824777077L;
	
	
	@Override
	protected String getAgentType() {
		return "GASearch";
	}
	
	@Override
	protected AgentInfo getAgentInfo() {
		
		return GASearch_Box.get();
	}


	/**
	 * @return	<code>true</code> if the current best error_rate is lower
	 * 			than the set threshold, or if the maximum number
	 *			of generations has been exceeded, 
	 *			<code>false</code> otherwise. 
	 */
	@Override
	protected boolean isFinished() {
		//number of generation, best error rate
		if (numberOfGenerations >= maximumGenerations) {
			return true;
		}

		if (bestErrorRate <= finalErrorRate) {			
			return true;
		}
		return false;

	}
	
	/**
	 * Generates a new population of the given size (for the first time), 
	 * or uses the individuals from the old population to create a new one.
	 * <p>
	 * Uses a selection {@link selectIndividual} with 
	 * elitism (the single best individual is kept), 
	 * mutation {@link mutateIndividual} 
	 * and crossover {@link xoverIndividuals}.
	 * 
	 * @param solutions     last solutions
	 * @param evaluations   last evaluations of above solutions
	 * @return              new solutions
	 */
	@Override
	protected List<SearchSolution> generateNewSolutions(
			List<SearchSolution> solutions, float[][] evaluations) {
		
		ArrayList<SearchSolution> newPopulation =
				new ArrayList<SearchSolution>(popSize);
		
		if(evaluations == null){
			//create new population			
			numberOfGenerations = 0;
			bestErrorRate = Double.MAX_VALUE;
			fitnesses = new float[popSize];
			for(int i = 0; i < popSize; i++){
				newPopulation.add(randomIndividual());
			}
		} else{
			//population from the old one
			//Elitism
			//1. find the best
			float bestFit = Float.MAX_VALUE;
			int bestIndex = -1;
			for(int i = 0; i < popSize; i++){
				if(fitnesses[i] < bestFit){
					bestFit = fitnesses[i];
					bestIndex = i;
				}
			}
			
			SearchSolution searchSolution =
					(SearchSolution) population.get(bestIndex);
			
			//To cloneSol by tu (asi) nemuselo byt...
			SearchSolution eliteInd = cloneSolution(searchSolution); 
			//2. put into new population
			newPopulation.add(eliteInd);
			for(int i = 0; i < ((popSize-1)/2);i++) {
				//pairs
				SearchSolution ind1 = cloneSolution(selectIndividual());
				SearchSolution ind2 = cloneSolution(selectIndividual());
				
				if(rndGen.nextDouble() < xoverProb){
					xoverIndividuals(ind1, ind2);
				}
				mutateIndividual(ind1);
				mutateIndividual(ind2);
				newPopulation.add(ind1);
				newPopulation.add(ind2);
			}
			if(((popSize-1)%2) == 1) {
				//one more, not in pair, if the pop is odd
				SearchSolution ind = cloneSolution(selectIndividual());
				mutateIndividual(ind);
				newPopulation.add(ind);
			}
		}
		numberOfGenerations++;
		population= newPopulation;
		return population;
	}

	/**
	 * Assigns evaluations to the population as fitnesses.
	 * 
	 * @return   The best (minimum) fitness in the current population. 
	 */
	@Override
	protected float updateFinished(float[][] evaluations) {				
		if(evaluations == null){
			for(int i = 0; i < popSize; i++){
				fitnesses[i]=1;
			}
		}else{
			for(int i = 0; i < evaluations.length; i++){				
				//fitness
				fitnesses[i]=evaluations[i][0];				
				//actualize bestErrorRate
				if(fitnesses[i]<bestErrorRate){
					bestErrorRate = fitnesses[i];
				}
			}
		}
		
		return (float) bestErrorRate;
	}

	
	/**
	 * Loads the parameters of the genetic algorithm search from its options.
	 * If the options are not set, sets the values to defaults:
	 * 
	 *  <ul>
	 *	 <li>popSize = 5 ... the same as queryBlockSize
	 *	 <li>mutProb = 0.2
	 *	 <li>xoverProb = 0.5
	 *	 <li>maximumGenerations = 10
	 *	 <li>finalErrorRate = 0.1
	 *	 <li>tournamentSize = 2
	 *  </ul>
	 */	
	@Override
	protected void loadSearchOptions() {
		popSize = 5;
		mutProb = 0.2;
		xoverProb = 0.5;
		maximumGenerations = 10;
		finalErrorRate = 0.1;
		tournamentSize = 2;
		
		// find maximum tries in Options		
		NewOptions options = getSearchOptions();
		
		if (options.containsOptionWithName("E")) {
			NewOption optionE = options.fetchOptionByName("E");
			FloatValue valueE =
					(FloatValue) optionE.toSingleValue().getCurrentValue();
			finalErrorRate = valueE.getValue(); 
		}
		if (options.containsOptionWithName("M")) {
			NewOption optionM = options.fetchOptionByName("M");
			IntegerValue valueM =
					(IntegerValue) optionM.toSingleValue().getCurrentValue();
			maximumGenerations = valueM.getValue(); 
		}		
		if (options.containsOptionWithName("T")) {
			NewOption optionT = options.fetchOptionByName("T");
			FloatValue valueT =
					(FloatValue) optionT.toSingleValue().getCurrentValue();
			mutProb = valueT.getValue(); 
		}
		if (options.containsOptionWithName("X")) {
			NewOption optionX = options.fetchOptionByName("X");
			FloatValue valueX =
					(FloatValue) optionX.toSingleValue().getCurrentValue();
			xoverProb = valueX.getValue(); 
		}
		if (options.containsOptionWithName("P")) {
			NewOption optionP = options.fetchOptionByName("P");
			IntegerValue valueP =
					(IntegerValue) optionP.toSingleValue().getCurrentValue();
			popSize = valueP.getValue(); 
		}
		if (options.containsOptionWithName("S")) {
			NewOption optionS = options.fetchOptionByName("S");
			IntegerValue valueS =
					(IntegerValue) optionS.toSingleValue().getCurrentValue();
			tournamentSize = valueS.getValue(); 
		}

		queryBlockSize = popSize;

	}

	/**
	 * Generates a new random options.
	 */
	private SearchSolution randomIndividual() {
		
		List<IValueData> newSolution = new ArrayList<IValueData>();
		for (SearchItem si : getSchema() ) {
            IValueData val = si.randomValue(rndGen);
			newSolution.add(val);
		}		
		SearchSolution resSol = new SearchSolution();
		resSol.setValues(newSolution);
		return resSol;
	}
	
	/**
	 * Tournament selection (minimization).
	 * 
	 * @return   the best SearchSolution in the population.
	 */
	private SearchSolution selectIndividual(){
		float best_fit = Float.MAX_VALUE;
		int bestIndex = -1;
		for(int i = 0; i < tournamentSize; i++){
			int ind= rndGen.nextInt(fitnesses.length);
			//MINIMIZATION!!!
			if(fitnesses[ind] <= best_fit){
				best_fit = fitnesses[ind];
				bestIndex = ind;
			}
		}
		return (SearchSolution) population.get(bestIndex);
	}
	
	/**
	 * Crossover of the sol1 an sol2 solutions. Changes the given solutions.
	 * Half uniform crossover.
	 * 
	 * @param sol1  solution 1 to be x-overed
	 * @param sol2  solution 2 to be x-overed
	 * 
	 */

	private void xoverIndividuals(SearchSolution sol1, SearchSolution sol2) {
		List<IValueData> newSolution1 = new ArrayList<IValueData>();
		List<IValueData> newSolution2 = new ArrayList<IValueData>();
		
		for (int i = 0; i < newSolution1.size(); i++) {
            IValueData val1 =  sol1.getValues().get(i);
            IValueData val2 =  sol2.getValues().get(i);
			if(rndGen.nextBoolean()){
				//The same...
				newSolution1.add(val1);
				newSolution2.add(val2);
			}else{
				//Gene exchange
				newSolution1.add(val2);
				newSolution2.add(val1);
			}
		}
		sol1.setValues(newSolution1);
		sol2.setValues(newSolution2);
	}
	
	/** 
	 * Mutation of the option. Changes the given solution.
	 * 
	 * @param solution     a solution to be mutated.
	 */
	private void mutateIndividual(SearchSolution solution){
		
		List<IValueData> newSolution = new ArrayList<IValueData>();
		for (int i = 0; i < getSchema().size(); i++ ) {
			
			SearchItem si = getSchema().get(i);
            IValueData val = solution.getValues().get(i);
			if(rndGen.nextDouble() < mutProb) {
				val = si.randomValue(rndGen);
			}
			newSolution.add(val);
		}
		solution.setValues(newSolution);
	}
	
	
	/**
	 * Clone options.
	 * 
	 * @param solution   a solution to be cloned.
	 * @return           a cloned solution.
	 */
	private SearchSolution cloneSolution(SearchSolution solution){
		List<IValueData> newSolution = solution.getValues();
		SearchSolution resSolution = new SearchSolution();
		resSolution.setValues(newSolution);
		return resSolution;
	}
	
}
