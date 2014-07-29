package org.pikater.core.ontology.subtrees.model;

import jade.content.Concept;

public class Model implements Concept {
	private static final long serialVersionUID = 4882357602886884881L;
	private int resultID;
	private String agentClassName;
	private byte[] serializedAgent;
	
	public Model(){}

	public Model(int resultID, String agentClassName, byte[] serializedAgent) {
		super();
		this.resultID = resultID;
		this.agentClassName = agentClassName;
		this.serializedAgent = serializedAgent;
	}

	public int getResultID() {
		return resultID;
	}

	public void setResultID(int resultID) {
		this.resultID = resultID;
	}

	public String getAgentClassName() {
		return agentClassName;
	}

	public void setAgentClassName(String agentClassName) {
		this.agentClassName = agentClassName;
	}

	public byte[] getSerializedAgent() {
		return serializedAgent;
	}

	public void setSerializedAgent(byte[] serializedAgent) {
		this.serializedAgent = serializedAgent;
	}
	
	public boolean isAgentType(Class<?> agentClass) {
		return this.agentClassName.equals(agentClass.getName());
	}
	
}