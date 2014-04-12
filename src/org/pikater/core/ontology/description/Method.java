package org.pikater.core.ontology.description;

import java.util.ArrayList;

import jade.content.Concept;

public class Method  implements Concept {
	
	String method;
    ArrayList<Parameter> parameters;

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

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void addParameter(Parameter parameter) {

		if (this.parameters == null) {
			this.parameters = new ArrayList<Parameter>();
		}
			
		this.parameters.add(parameter);
	}

}
