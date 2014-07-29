package org.pikater.core.ontology.subtrees.recommend;

import jade.content.AgentAction;

public class GetMultipleBestAgents implements AgentAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5959112027209211332L;

	private String nearestInternalFileName;
    private int numberOfAgents;

	public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

	public void setNearestInternalFileName(String nearestFileName) {
		this.nearestInternalFileName = nearestFileName;
	}

	public String getNearestInternalFileName() {
		return nearestInternalFileName;
	}
    
    

}