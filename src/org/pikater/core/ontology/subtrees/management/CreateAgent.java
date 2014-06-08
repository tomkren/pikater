package org.pikater.core.ontology.subtrees.management;

import org.pikater.core.agents.configuration.Argument;

import jade.content.AgentAction;
import org.pikater.core.agents.configuration.Arguments;

import java.util.List;

public class CreateAgent implements AgentAction {

	private static final long serialVersionUID = -5584350622664317441L;
	
	private String type;
	private String name;
	private Arguments arguments;
	
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setArguments(Arguments arguments) {
		this.arguments = arguments;
	}
	public Arguments getArguments() {
		return arguments;
	}
}
