package org.pikater.core.ontology.subtrees.management;

import org.pikater.core.agents.configuration.Argument;

import jade.content.AgentAction;
import java.util.List;

public class CreateAgent implements AgentAction {

	private static final long serialVersionUID = -5584350622664317441L;
	
	private String type;
	private String name;
	private List<Argument> arguments;
	
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
	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}
	public List<Argument> getArguments() {
		return arguments;
	}
}
