package org.pikater.core.ontology.subtrees.newOption.value;

import jade.content.Concept;

public interface IValue extends Concept {

	public String exportToWeka();
	public IValue cloneValue();
}
