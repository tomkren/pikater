package org.pikater.core.ontology.subtrees.metadata;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;

public class Metadatas implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7879865268316684239L;
	
	private List<Metadata> metadatas;
	
	public Metadatas(){
		this.metadatas=new ArrayList<Metadata>();
	}

	public List<Metadata> getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(List<Metadata> metadatas) {
		this.metadatas = metadatas;
	}
	
}
