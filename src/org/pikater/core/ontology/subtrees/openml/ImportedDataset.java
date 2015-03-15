package org.pikater.core.ontology.subtrees.openml;

import jade.content.Concept;

public class ImportedDataset implements Concept {

	private static final long serialVersionUID = 8958509539807108299L;
	
	public static int OK_FLAG = 0;
	//public static int MISSING_ID_FLAG = -1;
	public static int ERROR_FLAG = -2;

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	private int flag;

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
