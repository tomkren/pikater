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
	private SearchSolution new_solution = null;
	private float evaluation = Float.MAX_VALUE;
	private double temperature = 0.0;
	private double stability = 0.5;
	private int number_of_tries = 0;
	private int maximum_tries = 50;
	private double best_error_rate = 1;
	private double final_error_rate = 0.1;
	private boolean minimization = true;
	protected Random rnd_gen = new Random(1);

	@Override
	protected String getAgentType() {
		return "SimulatedAnnealing";
	}
	
	@Override
	protected AgentInfo getAgentInfo() {

		return SimulatedAnnealing_Box.get();
	}

	@Override
	protected boolean finished() {
		//n>=nmax
		if (number_of_tries >= maximum_tries) {
			return true;
		}
		if (best_error_rate <= final_error_rate){
			return true;
		}
		return false;
	}
	

	@Override
	protected void loadSearchOptions() {
		temperature = 1.0;//?
		maximum_tries = 50;
		stability = 0.5;
		final_error_rate = 0.01;
		
		NewOptions options = new NewOptions(getSearchOptions());
		
		NewOption optionE = options.fetchOptionByName("E");
		FloatValue valueE = (FloatValue) optionE.toSingleValue().getCurrentValue();
		final_error_rate = valueE.getValue(); 

		NewOption optionM = options.fetchOptionByName("M");
		IntegerValue valueM = (IntegerValue) optionM.toSingleValue().getCurrentValue();
		maximum_tries = valueM.getValue(); 

		NewOption optionS = options.fetchOptionByName("S");
		FloatValue valueS = (FloatValue) optionS.toSingleValue().getCurrentValue();
		stability = valueS.getValue(); 

		NewOption optionT = options.fetchOptionByName("T");
		FloatValue valueT = (FloatValue) optionT.toSingleValue().getCurrentValue();
		temperature = valueT.getValue(); 
		
	}

	@Override
	protected List<SearchSolution> generateNewSolutions(List<SearchSolution> solutions, float[][] evaluations) {
		
		if(evaluations == null){
			//inicializace
			solution = null;
			new_solution = null;
			evaluation = Float.MAX_VALUE;
			number_of_tries = 0;
			best_error_rate = Double.MAX_VALUE;
		}
		
		//create a new solution for evaluation
		new_solution = neighbor(solution);
		
		number_of_tries++;
		
		//List of solutions to send
		List<SearchSolution> solutions_list = new ArrayList<SearchSolution>();
		solutions_list.add(new_solution);
		return solutions_list;
	}
	
	@Override
	protected float updateFinished(float[][] evaluations) {
		float new_evaluation;
		
		if (evaluations == null){
			new_evaluation = Float.MAX_VALUE;
		}
		else{
			new_evaluation = evaluations[0][0];//((Evaluation)(evaluations.get(0))).getError_rate();
		}
		
		//Actualize best evaluation
		if(new_evaluation < best_error_rate){
			best_error_rate = new_evaluation;
		}
		//Acceptance of new solutions
		//System.out.print("<OK:> Temp:"+temperature+", e0: "+evaluation);
		if (rnd_gen.nextDouble()<(acceptanceProb(new_evaluation-evaluation,temperature))){
			solution = new_solution;
			evaluation = new_evaluation;
		}
		//System.out.println(", e1:"+ new_evaluation+", acceptance: "+ acc+" ,.5->1:"+ acceptanceProb(1-0.5,temperature)+" ,1->.5:"+ acceptanceProb(0.5-1,temperature));
		//Decrease temperature
		Cooling();
		
		return (float) best_error_rate;
	}
	
	//Neighbor function: Random solutions in case of beginning, or mutation of existing
	private SearchSolution neighbor(SearchSolution sol){

		List<IValueData> neighbourSolutionValues = new ArrayList<IValueData>();
		if(sol == null){
			//Completely new solution
			for (SearchItem si : getSchema() ) {
				//dont want to change old solutions
				neighbourSolutionValues.add(si.randomValue(rnd_gen));
			}
		}else{
			//Neighbor function
			for (int i = 0; i < getSchema().size(); i++) {
				
				SearchItem si = getSchema().get(i);
                IValueData val = sol.getValues().get(i);
				
				if(rnd_gen.nextDouble() > stability) {
					val = si.randomValue(rnd_gen);
				}
				neighbourSolutionValues.add(val);
			}
		}
		SearchSolution result = new SearchSolution();
		result.setValues(neighbourSolutionValues);
		return result;
	}
	
	/*Acceptance probability of annealed solutions: 
	  -the better values are accepted
	  -the worse with probability exp((e-e_new)/temperature)
	*/
	private double acceptanceProb(double delta, double temperature){
		if(!minimization){/*for max problems*/
			delta = -delta;
		}
		if(delta<0){//it is better
			return 1.0;
		}else{
			return Math.exp(-delta/temperature);
		}
	}
	
	//Cooling scheme: 20% decrease in each step	
	private void Cooling(){
		temperature = 0.8*temperature;
	}

}
