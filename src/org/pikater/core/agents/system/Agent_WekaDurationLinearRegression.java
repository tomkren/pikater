package org.pikater.core.agents.system;

import java.util.Date;

import org.pikater.core.agents.experiment.computing.Agent_WekaLinearRegression;
import org.pikater.core.agents.experiment.dataprocessing.communicator.ComputingCommunicator;
import org.pikater.core.ontology.subtrees.task.Evaluation;

public class Agent_WekaDurationLinearRegression extends
		Agent_WekaLinearRegression {
	
	private static final long serialVersionUID = 2468509141983327763L;

	@Override
	public Date train(Evaluation evaluation) throws Exception {
		Date date= super.train(evaluation);
		ComputingCommunicator cc=new ComputingCommunicator();
		cc.sendLastDuration(this);
		return date;
	}
	
	@Override	
	public void taskFinished() {
	}
	
}
