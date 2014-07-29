package org.pikater.core.ontology.subtrees.management;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;

public class Agents implements Concept {

	private static final long serialVersionUID = -8417148948375901354L;

	private List<Agent> agents;

	public Agents() { 
		this.agents=new ArrayList<Agent>();
	}

	public List<Agent> getAgents() {
		return agents;
	}

	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}

	public void add(Agent agent){
		this.agents.add(agent);
	}
}
