package org.pikater.core.ontology.subtrees.newOption.restrictions;

import org.pikater.core.ontology.subtrees.newOption.base.ICloneable;
import org.pikater.core.ontology.subtrees.newOption.base.IValidated;

import jade.content.Concept;

public interface IRestriction extends Concept, ICloneable, IValidated
{
	@Override
	public IRestriction clone();
}