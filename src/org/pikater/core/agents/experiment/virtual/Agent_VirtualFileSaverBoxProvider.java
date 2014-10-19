package org.pikater.core.agents.experiment.virtual;

import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.options.virtual.FileSaver_Box;

public class Agent_VirtualFileSaverBoxProvider extends Agent_VirtualBoxProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7569621739765373832L;

	@Override
	protected AgentInfo getAgentInfo() {

		return FileSaver_Box.get();
	}

}
