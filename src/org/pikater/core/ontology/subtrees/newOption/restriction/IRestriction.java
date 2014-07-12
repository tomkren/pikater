package org.pikater.core.ontology.subtrees.newOption.restriction;

import org.pikater.core.ontology.subtrees.newOption.type.Type;

import jade.content.Concept;

public interface IRestriction extends Concept {

	public Type getType();
	public boolean isValid();
}
