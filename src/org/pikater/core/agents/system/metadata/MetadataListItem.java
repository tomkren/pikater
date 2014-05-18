package org.pikater.core.agents.system.metadata;

import jade.util.leap.ArrayList;

import org.pikater.core.ontology.metadata.Metadata;

public class MetadataListItem {
	private Metadata metadata;
	private int id;
	public ArrayList to_compute = new ArrayList();
	
	public MetadataListItem(Metadata _metadata, int _id){
		setMetadata(_metadata);
		setId(_id);
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
