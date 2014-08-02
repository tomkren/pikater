package org.pikater.core.ontology.subtrees.recommend;

import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.management.Agent;

import jade.content.AgentAction;
import org.pikater.core.ontology.subtrees.task.Evaluation;

public class Recommend implements AgentAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4556943676301959461L;
	
	private Datas datas; 
	private Agent recommender;
    private Evaluation previousError;
	
	public Datas getDatas() {
		return datas;
	}
	public void setDatas(Datas datas) {
		this.datas = datas;
	}
	public Agent getRecommender() {
		return recommender;
	}
	public void setRecommender(Agent recommender) {
		this.recommender = recommender;
	}

    public Evaluation getPreviousError() {
        return previousError;
    }

    public void setPreviousError(Evaluation previousError) {
        this.previousError = previousError;
    }
}
