package org.pikater.core.ontology.subtrees.openml;

import jade.content.Concept;

public class ExportedDataset implements Concept {

	private static final long serialVersionUID = 8558494069264778520L;

	private int did;

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}
}
