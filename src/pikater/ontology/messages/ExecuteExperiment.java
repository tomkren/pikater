package pikater.ontology.messages;

import pikater.ontology.description.ComputationDescription;
import jade.content.AgentAction;

public class ExecuteExperiment implements AgentAction {

	private static final long serialVersionUID = 1L;
	private ComputationDescription description;

	public ExecuteExperiment(ComputationDescription description) {
		this.description = description;
	}

	public ExecuteExperiment() {
	}

	public ComputationDescription getDescription() {
		return description;
	}

	public void setDescription(ComputationDescription description) {
		this.description = description;
	}
	
}
