package org.pikater.core.ontology.messages;

import jade.content.AgentAction;

public class GetTheBestAgent implements AgentAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5959112027209211332L;
	private String _nearest_file_name;
    private int numberOfAgents;
    private String hash;

    public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    @Deprecated
	public void setNearest_file_name(String _nearest_file_name) {
		this._nearest_file_name = _nearest_file_name;
	}

    @Deprecated
	public String getNearest_file_name() {
		return _nearest_file_name;
	}
    
    

}