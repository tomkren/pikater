package org.pikater.core.ontology.subtrees.newOption.restrictions;

import org.pikater.core.ontology.subtrees.newOption.ICloneable;

import jade.content.Concept;

public interface IRestriction extends Concept, ICloneable
{
	public boolean isValid();
	public boolean isValidAgainst(Object obj);
	@Override
	public IRestriction clone();
}