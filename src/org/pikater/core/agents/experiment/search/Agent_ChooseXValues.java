package org.pikater.core.agents.experiment.search;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;
import org.pikater.core.options.ChooseXValue_SearchBox;

public class Agent_ChooseXValues extends Agent_Search {
	/*
	 * Implementation of simple tabulation of solutions 
	 */
	private static final long serialVersionUID = 838429530327268572L;
	private int n = Integer.MAX_VALUE;
	private int ni = 0;
	private int default_number_of_values_to_try = 5;

	private List<SearchSolution> solutions_list ;
	//private Vector<String> sub_options_vector ;

	@Override
	protected String getAgentType() {
		return "ChooseXValues";
	}
	
	@Override
	protected AgentInfo getAgentInfo() {

		return ChooseXValue_SearchBox.get();
	}

	@Override
	protected boolean finished() {
		if (ni < n) {
			return false;
		} else {
			return true;
		}
	}

	//TODO: Something less recursive
	private void generate(List<String> cur_solution_part,
			List<List<String>> possible_solution_values, int beg_ind) {
		
		if (possible_solution_values.size()-beg_ind < 1) {//if we are at the end
			
			List<String> vals = new ArrayList<String>();
			for (String valI : cur_solution_part) {
				vals.add(valI);
			}

			SearchSolution solution = new SearchSolution();
			//then solution part is whole solution
			solution.setValues(vals);
			
			solutions_list.add(solution);
			return;
		}
		
		List<String> pos_vals = possible_solution_values.get(beg_ind);
		for (int i = 0; i < pos_vals.size(); i++) {//For each possible value on the index beg_ind
			cur_solution_part.add(pos_vals.get(i));//append the value to the part of the solution
			
			generate(cur_solution_part,	possible_solution_values, beg_ind+1);//recursion
			cur_solution_part.remove(cur_solution_part.size()-1);//undo append
		}
	}


	private void generateSolutions_list(List<SearchItem> schema) {
		List<List<String>> possible_solutions =
				new ArrayList<List<String>>();
		
		for (SearchItem searchItemI : schema) {
			if (searchItemI.getNumber_of_values_to_try() == 0) {
				searchItemI.setNumber_of_values_to_try(default_number_of_values_to_try);
			}
			possible_solutions.add(searchItemI.possibleValues());
		}
		generate(new ArrayList<String>(), possible_solutions, 0);
		n = solutions_list.size();
	}

	@Override
	protected List<SearchSolution> generateNewSolutions(List<SearchSolution> solutions, float[][] evaluations) {
		
		if (n == 0)
			return null;
		/*SearchSolution new_solution = (SearchSolution)solutions_list.get(ni++);
		new ArrayList();
		res_solutions.add(new_solution);*/
		ni+=n;
		return solutions_list;
	}


	@Override
	protected void loadSearchOptions() {
		List<NewOption> search_options = getSearchOptions();
		
		for (NewOption next : search_options) {

			if (next.getName().equals("N")){
				IntegerValue value = (IntegerValue) next.toSingleValue().getCurrentValue();
				default_number_of_values_to_try = value.getValue();
			}
		}
		List<SearchItem> schema = getSchema();
		n = Integer.MAX_VALUE;
		ni = 0;
		solutions_list = new ArrayList<SearchSolution>();
		generateSolutions_list(schema);
		query_block_size = n;
	}

	@Override
	protected void updateFinished(float[][] evaluations) {
		//???
	}

}
