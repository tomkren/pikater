package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

import jade.content.Concept;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public interface IComputationElement extends Concept {
	
	public int getId();
	public void setId(int id);
	
	public List<NewOption> exportAllOptions();
	public void importAllOptions(List<NewOption> options);
	
	public List<ErrorSourceDescription> exportAllErrors();
	public void importAllErrors(List<ErrorSourceDescription> errors);
	
	public List<DataSourceDescription> exportAllDataSourceDescriptions();
	public void importAllDataSourceDescriptions(List<DataSourceDescription> dataSourceDescriptions);
	
	public UniversalOntology exportUniversalOntology();
	//public void importUniversalOntology(UniversalOntology uOntology);
	
	public boolean equalsElement(IComputationElement element);
	
	public IComputationElement clone();
	public void cloneSources();
}
