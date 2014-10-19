package org.pikater.core.ontology.subtrees.newoption.values.interfaces;

import org.pikater.core.ontology.subtrees.newoption.base.IWekaItem;
import org.pikater.shared.util.ICloneable;

import jade.content.Concept;

public interface IValueData extends Concept, ICloneable, IWekaItem {
	public int hashCode();

	public boolean equals(Object obj);

	public Object hackValue();

	public String toDisplayName();

	public IValueData clone();
}