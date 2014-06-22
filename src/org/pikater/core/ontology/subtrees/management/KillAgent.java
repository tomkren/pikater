package org.pikater.core.ontology.subtrees.management;

import jade.content.AgentAction;

public class KillAgent implements AgentAction {

	private static final long serialVersionUID = -5584350622664317441L;

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
