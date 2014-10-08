package org.pikater.core.agents.experiment.search;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;
import org.pikater.core.options.search.ChooseXValue_Box;

/*
 * 
 * Implementation of simple tabulation of solutions
 * 
 */
public class Agent_ChooseXValues extends Agent_Search {

	private static final long serialVersionUID = 838429530327268572L;
	
	private int n = Integer.MAX_VALUE;
	private int ni = 0;
	private int defaultNumberOfValuesToTry = 5;

	private List<SearchSolution> solutionsList;

	@Override
	protected String getAgentType() {
		return "ChooseXValues";
	}
	
	@Override
	protected AgentInfo getAgentInfo() {
		return ChooseXValue_Box.get();
	}

	@Override
	protected boolean isFinished() {
		if (ni < n) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 * Generate solution
	 * 
	 */
	private void generate(List<IValueData> curSolutionPart,
			List<List<IValueData>> possibleSolutionValues, int begInd) {
		
		if (possibleSolutionValues.size()-begInd < 1) {
			// at the end
			
			List<IValueData> vals = new ArrayList<IValueData>();
			for (IValueData valI : curSolutionPart) {
				vals.add(valI);
			}

			SearchSolution solution = new SearchSolution();
			//then solution part is whole solution
			solution.setValues(vals);
			
			solutionsList.add(solution);
			return;
		}
		
		List<IValueData> posVals = possibleSolutionValues.get(begInd);
		
		// for each possible value on the index begInd
		for (int i = 0; i < posVals.size(); i++) {
			
			// append the value to the part of the solution
			curSolutionPart.add(posVals.get(i));
			
			// recursion
			generate(curSolutionPart, possibleSolutionValues, begInd+1);
			
			// undo append
			curSolutionPart.remove(curSolutionPart.size()-1);
		}
	}

	/**
	 * Generate solutions
	 * 
	 */
	private void generateSolutionsList(List<SearchItem> schema) {
		List<List<IValueData>> possibleSolutions =
				new ArrayList<List<IValueData>>();
		
		for (SearchItem searchItemI : schema) {
			if (searchItemI.getNumberOfValuesToTry() == 0) {
				searchItemI.setNumberOfValuesToTry(defaultNumberOfValuesToTry);
			}
			possibleSolutions.add(searchItemI.possibleValues());
		}
		generate(new ArrayList<IValueData>(), possibleSolutions, 0);
		n = solutionsList.size();
	}

	@Override
	protected List<SearchSolution> generateNewSolutions(
			List<SearchSolution> solutions, float[][] evaluations) {
		
		if (n == 0) {
			return new ArrayList<SearchSolution>();
		}
		/*SearchSolution new_solution = (SearchSolution)solutions_list.get(ni++);
		new ArrayList();
		res_solutions.add(new_solution);*/
		ni += n;
		
		return solutionsList;
	}


	@Override
	protected void loadSearchOptions() {
		
		NewOptions searchOptions = getSearchOptions();
		
		if (searchOptions.containsOptionWithName("N")) {
			NewOption optionN = searchOptions.fetchOptionByName("N");
			IntegerValue valueN = (IntegerValue)
					optionN.toSingleValue().getCurrentValue();
			defaultNumberOfValuesToTry = valueN.getValue(); 
		}	
		
		List<SearchItem> schema = getSchema();
		n = Integer.MAX_VALUE;
		ni = 0;
		solutionsList = new ArrayList<SearchSolution>();
		generateSolutionsList(schema);
		queryBlockSize = n;
	}

	@Override	
	protected float updateFinished(float[][] evaluations) {
		return 1;
	}

}
