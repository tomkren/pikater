package org.pikater.core.agents.experiment.virtual;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.FileInput_VirtualBox;

public class Agent_VirtualFileInputBoxProvider extends Agent_VirtualBoxProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7569621739765373832L;

	@Override
	protected AgentInfo getAgentInfo() {

		return FileInput_VirtualBox.get();
	}

}
