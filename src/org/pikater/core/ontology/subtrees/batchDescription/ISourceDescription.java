package org.pikater.core.ontology.subtrees.batchDescription;

import jade.content.Concept;

public interface ISourceDescription extends Concept {

    public String getOutputType();
    public void setOutputType(String dataType);
    
    public String getInputType();
	public void setInputType(String dataInputType);
    
	public void importSource(IComputationElement element); 
	public IComputationElement exportSource(); 
}
