package org.pikater.core.agents.system;

import java.util.Date;

import org.pikater.core.agents.experiment.computing.Agent_WekaLinearRegression;
import org.pikater.core.agents.experiment.computing.communicator.ComputingCommunicator;
import org.pikater.core.ontology.subtrees.task.Evaluation;

/**
 * 
 * Computing agent used for a performance measuring,
 * solves a Linear Regression Task
 *
 */
public class Agent_WekaDurationLinearRegression extends
		Agent_WekaLinearRegression {
	
	private static final long serialVersionUID = 2468509141983327763L;

	private String DurationServiceRegression_output_prefix = "  --d-- ";
	
	@Override
	public Date train(Evaluation evaluation) throws Exception {
		Date date = super.train(evaluation);
		ComputingCommunicator.sendLastDuration(this);
		return date;
	}
	
	@Override	
	public void logStartTask() {
		logInfo(DurationServiceRegression_output_prefix);
	}

	@Override	
	public void logOptions() {
		logInfo(DurationServiceRegression_output_prefix + getOptions());
	}
	
	@Override	
	public void logFinishedTask() {
	}
	
}
