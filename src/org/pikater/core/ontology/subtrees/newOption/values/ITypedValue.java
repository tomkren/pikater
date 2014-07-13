package org.pikater.core.ontology.subtrees.newOption.values;

import jade.content.Concept;

public interface ITypedValue extends Concept {

	public String toDisplayName();
	public String exportToWeka();
	public ITypedValue clone();
}
