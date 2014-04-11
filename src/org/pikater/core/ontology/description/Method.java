package org.pikater.core.ontology.description;

import jade.content.Concept;

public class Method  implements Concept {
	
	String method;

	public Method() {}
	
	public Method(String method) {
		this.method = method;
	}
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

}
