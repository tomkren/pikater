package org.pikater.core.ontology.subtrees.newOption.type;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;

public class Types implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 236577012228824852L;
	
	private List<Type> types;

	public Types() {}
	public Types(List<Type> types) {
		setTypes(types);
	}
	
	public List<Type> getTypes() {
		return types;
	}
	public void setTypes(List<Type> types) {
		this.types = types;
	}
	
	public Types cloneTypes() {
			
		List<Type> typeList = new ArrayList<Type>();
		for (Type typeI : types) {
			typeList.add(typeI);
		}
		
		Types typesNew = new Types();
		typesNew.setTypes(typeList);

		return typesNew;
	}
}
