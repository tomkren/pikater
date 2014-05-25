package org.pikater.core.ontology.subtrees.newOption.type;

import jade.content.Concept;

public class Type implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4658896847448815807L;

	private String className;

	public Type() {}
	public Type(Class<?> classs) {
		this.className = classs.getName();
	}

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setClassName(Class<?> classs) {
		this.className = classs.getName();
	}

}
