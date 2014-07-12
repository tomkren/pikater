package org.pikater.core.ontology.subtrees.newOption.restrictions;

import org.pikater.core.ontology.subtrees.newOption.ValueType;

import jade.content.Concept;

public interface IRestriction extends Concept {

	public ValueType getType();
	public boolean isValid();
}
