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
import org.pikater.core.options.search.RandomSearch_Box;

public class Agent_RandomSearch extends Agent_Search {

	private static final long serialVersionUID = 2777277001533605329L;

	private int numberOfTries = 0;
	private float errorRate = 1;
	
	private int maximumTries;
	private float finalErrorRate;
	
	protected Random rndGen = new Random(1);

	@Override
	protected String getAgentType() {
		return "RandomSearch";
	}

	@Override
	protected AgentInfo getAgentInfo() {
		return RandomSearch_Box.get();
	}

	@Override
	protected boolean isFinished() {
		if (numberOfTries >= maximumTries) {
			return true;
		}

		if (errorRate <= finalErrorRate) {
			return true;
		}
		return false;
	}

	@Override
	protected void loadSearchOptions(){
		
		finalErrorRate = (float) 0.01;
		maximumTries= 10;
		
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
			maximumTries = valueM.getValue(); 
		}	
		
		queryBlockSize = maximumTries;
		logInfo(getLocalName()+" parameters are: ");
		logInfo("   finalErrorRate: " + finalErrorRate);
		logInfo("   maximumTries: " + maximumTries);		
	}

	@Override
	protected float updateFinished(float[][] evaluations) {
		if (evaluations == null){
			errorRate = 1;
		}
		else{
			float bestError = evaluations[0][0];
			for(int i = 0; i < evaluations.length; i++){
				if(evaluations[i][0]<bestError)
					bestError = evaluations[i][0];
			}
			errorRate = bestError;		
		}
		return errorRate;
	}
	
	private SearchSolution genRandomSolution(){
		// go through the solutions Vector, generate random values
		List<IValueData> new_solution = new ArrayList<IValueData>();
        List<String> names = new ArrayList<String>();
		for (SearchItem si : getSchema() ) {
			new_solution.add(si.randomValue(rndGen));
            names.add(si.getName());
		}
		SearchSolution sol = new SearchSolution();
		sol.setValues(new_solution);
		return sol;
	}
		
	@Override
	protected List<SearchSolution> generateNewSolutions(
			List<SearchSolution> solutions, float[][] evaluations) {
		
		numberOfTries += queryBlockSize;
		
		List<SearchSolution> solutionsList = new ArrayList<SearchSolution>();
		//generate sequence of random solutions
		for (int i = 0; i < queryBlockSize; i++) {
			solutionsList.add(genRandomSolution());
		}
		return solutionsList;
	}	
}