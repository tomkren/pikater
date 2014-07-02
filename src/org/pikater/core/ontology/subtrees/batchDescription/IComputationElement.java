package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.List;

import org.pikater.core.ontology.subtrees.batchDescription.export.Slot;
import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

import jade.content.Concept;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public interface IComputationElement extends Concept {
	
	public int getId();
	public void setId(int id);
	
	public List<Option> getUniversalOptions();
	public List<ErrorDescription> getUniversalErrors();
	public List<Slot> getInputSlots();

	public void setUniversalOptions(List<Option> options);
	public void setUniversalErrors(List<ErrorDescription> errors);
	public void setUniversalInputSlots(List<Slot> universalInputSlots);
	
	public UniversalOntology exportUniversalElement();
}
