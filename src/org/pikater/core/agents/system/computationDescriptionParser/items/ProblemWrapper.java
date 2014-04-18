package org.pikater.core.agents.system.computationDescriptionParser.items;

import org.pikater.core.agents.system.computationDescriptionParser.ItemOfGraph;
import org.pikater.core.ontology.messages.Problem;

public class ProblemWrapper extends ItemOfGraph {

	private Problem problem = null;

	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
}
