package org.pikater.core.agents.experiment.search;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.Options;
import org.pikater.core.ontology.subtrees.newOption.Value;
import org.pikater.core.ontology.subtrees.newOption.typedValue.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;
import org.pikater.core.options.GASearch_SearchBox;


public class Agent_GASearch extends Agent_Search {
	/*
	 * Implementation of Genetic algorithm search
	 * Half uniform crossover, tournament selection
	 * Options:
	 * -E float
	 * minimum error rate (default 0.1)
	 * 
	 * -M int 
	 * maximal number of generations (default 10)
	 * 
	 * -T float
	 * Mutation rate (default 0.2)
	 * 
	 * -X float
	 * Crossover probability (default 0.5)
	 * 
	 * -P int
	 * population size (default 5)
	 * 
	 * -S int
	 * Size of tournament in selection (default 2)
	 */
	private ArrayList<SearchSolution> population;
	//fitness is the error rate - the lower, the better!
	float fitnesses[];
	int pop_size = 0;
	double mut_prob = 0.0;
	double xover_prob = 0.0;
	private int number_of_generations = 0;
	private double best_error_rate = Double.MAX_VALUE;
	
	private int maximum_generations;
	private double final_error_rate;
	int tournament_size = 2;
	protected Random rnd_gen = new Random(1);

	/**
	 * 
	 */
	private static final long serialVersionUID = -387458001824777077L;
	
	
	@Override
	protected String getAgentType() {
		return "GASearch";
	}
	
	@Override
	protected AgentInfo getAgentInfo() {
		
		return GASearch_SearchBox.get();
	}

	@Override
	protected boolean finished() {
		//number of generation, best error rate
		if (number_of_generations >= maximum_generations) {
			return true;
		}

		if (best_error_rate <= final_error_rate) {			
			return true;
		}
		return false;

	}
	
	@Override
	protected List<SearchSolution> generateNewSolutions(List<SearchSolution> solutions, float[][] evaluations) {
		ArrayList<SearchSolution> new_population = new ArrayList<SearchSolution>(pop_size);
		if(evaluations==null){
			//create new population			
			number_of_generations = 0;
			best_error_rate = Double.MAX_VALUE;
			fitnesses = new float[pop_size];
			for(int i = 0; i < pop_size; i++){
				new_population.add(randomIndividual());
			}
		} else{
			//population from the old one
			//Elitism
			//1. find the best
			float best_fit = Float.MAX_VALUE;
			int best_index = -1;
			for(int i = 0; i < pop_size; i++){
				if(fitnesses[i] < best_fit){
					best_fit = fitnesses[i];
					best_index = i;
				}
			}
			SearchSolution elite_ind = cloneSol((SearchSolution) population.get(best_index)); //To cloneSol by tu (asi) nemuselo byt...
			//2. put into new population
			new_population.add(elite_ind);
			for(int i = 0; i < ((pop_size-1)/2);i++){
				//pairs
				SearchSolution ind1 = cloneSol(selectIndividual());
				SearchSolution ind2 = cloneSol(selectIndividual());
				
				if(rnd_gen.nextDouble()<xover_prob){
					xoverIndividuals(ind1, ind2);
				}
				mutateIndividual(ind1);
				mutateIndividual(ind2);
				new_population.add(ind1);
				new_population.add(ind2);
			}
			if(((pop_size-1)%2)==1){
				//one more, not in pair, if the pop is odd
				SearchSolution ind = cloneSol(selectIndividual());
				mutateIndividual(ind);
				new_population.add(ind);
			}
		}
		number_of_generations++;
		population= new_population;
		return population;
	}

	@Override
	protected void updateFinished(float[][] evaluations) {
		//assign evaluations to the population as fitnesses		
		if(evaluations == null){
			for(int i = 0; i < pop_size; i++){
				fitnesses[i]=1;
			}
		}else{
			for(int i = 0; i < evaluations.length; i++){				
				//fitness
				fitnesses[i]=evaluations[i][0];//((Evaluation)(evaluations.get(i))).getError_rate();				
				//actualize best_error_rate
				if(fitnesses[i]<best_error_rate){
					best_error_rate = fitnesses[i];
				}
			}
		}

	}

	@Override
	protected void loadSearchOptions() {
		pop_size = 5;
		mut_prob = 0.2;
		xover_prob = 0.5;
		maximum_generations = 10;
		final_error_rate = 0.1;
		tournament_size = 2;
		
		// find maximum tries in Options		
		Options options = new Options(getSearchOptions());
		
		if (options.containsOptionWithName("E")) {
			NewOption optionE = options.getOptionByName("E");
			FloatValue valueE = (FloatValue) optionE.convertToSingleValue().getTypedValue();
			final_error_rate = valueE.getValue(); 
		}
		if (options.containsOptionWithName("M")) {
			NewOption optionM = options.getOptionByName("M");
			IntegerValue valueM = (IntegerValue) optionM.convertToSingleValue().getTypedValue();
			maximum_generations = valueM.getValue(); 
		}		
		if (options.containsOptionWithName("T")) {
			NewOption optionT = options.getOptionByName("T");
			FloatValue valueT = (FloatValue) optionT.convertToSingleValue().getTypedValue();
			mut_prob = valueT.getValue(); 
		}
		if (options.containsOptionWithName("X")) {
			NewOption optionX = options.getOptionByName("X");
			FloatValue valueX = (FloatValue) optionX.convertToSingleValue().getTypedValue();
			xover_prob = valueX.getValue(); 
		}
		if (options.containsOptionWithName("P")) {
			NewOption optionP = options.getOptionByName("P");
			IntegerValue valueP = (IntegerValue) optionP.convertToSingleValue().getTypedValue();
			pop_size = valueP.getValue(); 
		}
		if (options.containsOptionWithName("S")) {
			NewOption optionS = options.getOptionByName("S");
			IntegerValue valueS = (IntegerValue) optionS.convertToSingleValue().getTypedValue();
			tournament_size = valueS.getValue(); 
		}

		query_block_size = pop_size;

	}

	//new random options
	private SearchSolution randomIndividual() {
		
		List<String> new_solution = new ArrayList<String>();
		for (SearchItem si : getSchema() ) {
			String val = si.randomValue(rnd_gen);
			new_solution.add(val);
		}		
		SearchSolution res_sol = new SearchSolution();
		res_sol.setValues(new_solution);
		return res_sol;
	}
	
	//tournament selection (minimization)
	private SearchSolution selectIndividual(){
		float best_fit = Float.MAX_VALUE;
		int best_index = -1;
		for(int i = 0; i < tournament_size; i++){
			int ind= rnd_gen.nextInt(fitnesses.length);
			//MINIMIZATION!!!
			if(fitnesses[ind] <= best_fit){
				best_fit = fitnesses[ind];
				best_index = ind;
			}
		}
		return (SearchSolution) population.get(best_index);
	}
	
	//Half uniform crossover
	private void xoverIndividuals(SearchSolution sol1, SearchSolution sol2){
		List<String> new_solution1 = new ArrayList<String>();
		List<String> new_solution2 = new ArrayList<String>();
		
		for (int i = 0; i < new_solution1.size(); i++) {
			String val1 = (String) sol1.getValues().get(i);
			String val2 = (String) sol2.getValues().get(i);
			if(rnd_gen.nextBoolean()){
				//The same...
				new_solution1.add(val1);
				new_solution2.add(val2);
			}else{
				//Gene exchange
				new_solution1.add(val2);
				new_solution2.add(val1);
			}
		}
		sol1.setValues(new_solution1);
		sol2.setValues(new_solution2);
	}
	
	//mutation of the option
	private void mutateIndividual(SearchSolution sol){
		
		List<String> new_sol = new ArrayList<String>();
		for (int i = 0; i < getSchema().size(); i++ ) {
			
			SearchItem si = getSchema().get(i);
			String val = ((String) sol.getValues().get(i));
			if(rnd_gen.nextDouble() < mut_prob)
				val = si.randomValue(rnd_gen);
			new_sol.add(val);
		}
		sol.setValues(new_sol);
	}
	
	
	//Clone options
	private SearchSolution cloneSol(SearchSolution sol){
		List<String> new_solution = sol.getValues();
		SearchSolution res_sol = new SearchSolution();
		res_sol.setValues(new_solution);
		return res_sol;
	}
	
}
