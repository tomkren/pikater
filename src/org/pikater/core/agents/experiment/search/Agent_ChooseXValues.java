package org.pikater.core.agents.experiment.search;

import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.ontology.messages.option.Option;
import org.pikater.core.ontology.messages.search.SearchSolution;
import org.pikater.core.ontology.messages.searchItems.SearchItem;
import org.pikater.core.options.ChooseXValue_SearchBox;

public class Agent_ChooseXValues extends Agent_Search {
	/*
	 * Implementation of simple tabulation of solutions 
	 */
	private static final long serialVersionUID = 838429530327268572L;
	private int n = Integer.MAX_VALUE;
	private int ni = 0;
	private int default_number_of_values_to_try = 5;

	private List solutions_list ;
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
	private void generate(java.util.List<String> cur_solution_part,
			java.util.List<java.util.List<String>> possible_solution_values, int beg_ind) {
		
		if (possible_solution_values.size()-beg_ind < 1) {//if we are at the end
			
			java.util.List<String> vals = new java.util.ArrayList<String>();
			for (String valI : cur_solution_part) {
				vals.add(valI);
			}

			SearchSolution solution = new SearchSolution();
			//then solution part is whole solution
			solution.setValues(vals);
			
			solutions_list.add(solution);
			return;
		}
		
		java.util.List<String> pos_vals = (java.util.List)possible_solution_values.get(beg_ind);
		for (int i = 0; i < pos_vals.size(); i++) {//For each possible value on the index beg_ind
			cur_solution_part.add(pos_vals.get(i));//append the value to the part of the solution
			
			generate(cur_solution_part,	possible_solution_values, beg_ind+1);//recursion
			cur_solution_part.remove(cur_solution_part.size()-1);//undo append
		}
	}


	private void generateSolutions_list(java.util.List<SearchItem> schema) {
		java.util.List<java.util.List<String>> possible_solutions =
				new java.util.ArrayList<java.util.List<String>>();
		
		for (SearchItem searchItemI : schema) {
			if (searchItemI.getNumber_of_values_to_try() == 0) {
				searchItemI.setNumber_of_values_to_try(default_number_of_values_to_try);
			}
			possible_solutions.add(searchItemI.possibleValues());
		}
		generate(new java.util.ArrayList<String>(), possible_solutions, 0);
		n = solutions_list.size();
	}

	@Override
	protected List generateNewSolutions(List solutions, float[][] evaluations) {
		
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
		java.util.List<Option> search_options = getSearch_options();
		
		for (Option next : search_options) {

			if (next.getName().equals("N")){
				default_number_of_values_to_try = Integer.parseInt(next.getValue());
			}
		}
		java.util.List<SearchItem> schema = getSchema();
		n = Integer.MAX_VALUE;
		ni = 0;
		solutions_list = new ArrayList();
		generateSolutions_list(schema);
		query_block_size = n;
	}

	@Override
	protected void updateFinished(float[][] evaluations) {
		//???
	}

}
