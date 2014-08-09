package org.pikater.core.ontology.subtrees.duration;

import jade.content.AgentAction;

public class SetDuration implements AgentAction {

	private static final long serialVersionUID = 7932839321818527345L;
	
	private Duration duration;

	public SetDuration(){
		this.duration=new Duration();
	}
	
	public Duration getDuration() {
		return duration;
	}
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	
}