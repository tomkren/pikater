package org.pikater.core.agents.experiment.search;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newoption.NewOptions;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchitems.SearchItem;
import org.pikater.core.options.search.RandomSearch_Box;

/**
 * Implementation of Random search.
 * <p>
 * Options:
 * <ul>
 *   <li>-E float   Minimum error rate (default 0.1)
 *   <li>-M int     Maximum number of generations (default 10)
 * </ul>  
 */
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

	/**
	 * @return	<code>true</code> if the current error_rate is lower
	 * 			than the set threshold, or if the maximum number
	 *			of tries has been exceeded, 
	 *			<code>false</code> otherwise. 
	 */
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

	/**
	 * Loads the parameters of random search from search's options.
	 * If the options are not set, sets the values to defaults:
	 * 
	 *  <ul>
	 *	 <li>maximumTries = 10;
	 *   <li>finalErrorRate = 0.01;
	 *  </ul>
	 */
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
				if(evaluations[i][0] < bestError) {
					bestError = evaluations[i][0];
				}
			}
			errorRate = bestError;		
		}
		return errorRate;
	}
	
	/**
	 * Goes through the solutions list, generates random values.
	 * 
	 * @return SearchSolution   a new random solution to try.
	 */
	private SearchSolution genRandomSolution() {
		List<IValueData> newSolution = new ArrayList<IValueData>();
        List<String> names = new ArrayList<String>();
		for (SearchItem si : getSchema() ) {
			newSolution.add(si.randomValue(rndGen));
            names.add(si.getName());
		}
		SearchSolution sol = new SearchSolution();
		sol.setValues(newSolution);
		return sol;
	}
		
	
	/**
	 * Generates a list of new random solutions. 
	 * The size of the list (number of solutions) is the queryBlockSize.
	 * 
	 * @param solutions     last solutions
	 * @param evaluations   last evaluations of above solutions
	 * @return              a list of newly generated solutions.
	 */
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