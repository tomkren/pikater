package org.pikater.core.agents.experiment.virtual;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.CARecSearchComplex_VirtualBox;

public class Agent_VirtualCARecSearchComplexBoxProvider extends
		Agent_VirtualBoxProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7569621739765373832L;

	@Override
	protected AgentInfo getAgentInfo() {

		return CARecSearchComplex_VirtualBox.get();
	}

}
