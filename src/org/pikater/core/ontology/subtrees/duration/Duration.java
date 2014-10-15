package org.pikater.core.ontology.subtrees.duration;

import java.util.Date;
import jade.content.Concept;

public class Duration implements Concept {

	private static final long serialVersionUID = -7310795521154346932L;
	
	private Date start;
	private long durationMiliseconds;
	private float durationLR;

	public float getLR_duration() {
		return durationLR;
	}

	public void setdurationLR(float durationLR) {
		this.durationLR = durationLR;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public long getDurationMiliseconds() {
		return durationMiliseconds;
	}

	public void setDurationMiliseconds(long duration) {
		this.durationMiliseconds = duration;
	}

}