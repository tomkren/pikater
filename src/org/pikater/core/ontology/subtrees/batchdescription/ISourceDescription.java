package org.pikater.core.ontology.subtrees.batchdescription;

import org.pikater.shared.util.ICloneable;

import jade.content.Concept;

public interface ISourceDescription extends Concept, ICloneable
{
    String getOutputType();
    void setOutputType(String dataType);
    
    String getInputType();
	void setInputType(String dataInputType);
    
	void importSource(IComputationElement element); 
	IComputationElement exportSource();
	
	@Override
	ISourceDescription clone();
}