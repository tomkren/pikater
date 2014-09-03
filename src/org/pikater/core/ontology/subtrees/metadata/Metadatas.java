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
	
	public Metadatas() {
		this.metadatas = new ArrayList<Metadata>();
	}

	public List<Metadata> getMetadatas() {
		return metadatas;
	}
	public void setMetadatas(List<Metadata> metadatas) {
		this.metadatas = metadatas;
	}
	public void addMetadata(Metadata metadata) {
		
		if (! contains(metadata)) {
			this.metadatas.add(metadata);
		}
	}
	
	public boolean contains(Metadata metadata) {
		
		String hash = metadata.getInternalName();
		
		for (Metadata metadataI : this.metadatas) {
			String hashI = metadataI.getInternalName();
			if (hashI.equals(hash)) {
				return true;
			}
		}
		
		return false;
	}
	
}
