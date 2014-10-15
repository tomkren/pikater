package org.pikater.core.agents.system.metadata;

import jade.util.leap.ArrayList;

import org.pikater.core.ontology.subtrees.metadata.Metadata;

public class MetadataListItem {
	private Metadata metadata;
	private int id;
	public ArrayList toCompute = new ArrayList();
	
	public MetadataListItem(Metadata metadata, int id){
		setMetadata(metadata);
		setId(id);
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
