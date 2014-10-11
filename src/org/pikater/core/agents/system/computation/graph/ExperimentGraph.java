package org.pikater.core.agents.system.computation.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Experiment graph.
 */
public class ExperimentGraph {
    /**
     * The Id.
     */
    protected Integer id;
	
	/*
	 * Top layer of problem tree
	 */
	private List<ExperimentItem> problems = new ArrayList<>();

    /**
     * Gets problems.
     *
     * @return the problems
     */
    public List<ExperimentItem> getProblems() {
		return problems;
	}

    /**
     * Sets problems.
     *
     * @param problems the problems
     */
    public void setProblems(ArrayList<ExperimentItem> problems) {
		this.problems = problems;
	}

    /**
     * Add root problem.
     *
     * @param problem the problem
     */
    public void addRootProblem(ExperimentItem problem) {
		this.problems.add(problem);
	}

    /**
     * Test if all problemItems in dependency graph are finished
     *
     * @return the boolean
     */
	public boolean areAllProblemsFinished() {
		
		// if exists some rootItem which is not finished
		for (ExperimentItem problemI : problems) {
			
			if (problemI.getStatus() != ExperimentItem.ProblemStatus.IS_FINISHED) {
				return false;
			}
		}
		return true;
	}
}
