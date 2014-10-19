package org.pikater.core.agents.system.manager.graph.edges;

/**
 * Edge with agent type
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:36
 */
public class AgentTypeEdge extends EdgeValue {
	private String agentType;

    //TODO: looks obsolete
	private Integer model;

    /**
     * Instantiates a new Agent type edge.
     *
     * @param agentType Agent type
     */
	public AgentTypeEdge(String agentType) {
		this.agentType = agentType;
	}

    /**
     * Instantiates a new Agent type edge.
     *
     * @param agentType the agent type
     * @param model the model
     */
    public AgentTypeEdge(String agentType, Integer model) {
		this(agentType);
		this.model = model;
	}

    /**
     * Gets agent type.
     *
     * @return the agent type
     */
    public String getAgentType() {
		return agentType;
	}

    /**
     * Sets agent type.
     *
     * @param agentType the agent type
     */
    public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

    /**
     * Gets model.
     *
     * @return the model
     */
    public Integer getModel() {
		return model;
	}

    /**
     * Sets model.
     *
     * @param model the model
     */
    public void setModel(Integer model) {
		this.model = model;
	}
}
