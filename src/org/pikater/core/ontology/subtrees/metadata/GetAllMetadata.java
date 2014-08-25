package org.pikater.core.ontology.subtrees.metadata;

import java.util.ArrayList;
import java.util.List;

import jade.content.AgentAction;

public class GetAllMetadata implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5223603055736238437L;
	
	private List<Metadata> exceptions;
	private boolean resultsRequired;
	
	public GetAllMetadata() {
		this.exceptions = new ArrayList<Metadata>();
	}
	
	public List<Metadata> getExceptions() {
		return exceptions;
	}
	public void setExceptions(List<Metadata> exceptions) {
		this.exceptions = exceptions;
	}
	
	public boolean getResultsRequired() {
		return resultsRequired;
	}
	public void setResultsRequired(boolean resultsRequired) {
		this.resultsRequired = resultsRequired;
	}
	
}
