package org.pikater.core.ontology.subtrees.newOption.values.interfaces;

import org.pikater.core.ontology.subtrees.newOption.ICloneable;

import jade.content.Concept;

public interface IValueData extends Concept, ICloneable
{
	public Object getValue();
	public String toDisplayName();
	public String exportToWeka();
	public IValueData clone();
}