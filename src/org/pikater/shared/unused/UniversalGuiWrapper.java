package org.pikater.shared.unused;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.pikater.shared.experiment.UniversalElement;

public class UniversalGuiWrapper
{
	private UniversalGuiWrapper parentWrapper; 
	private final Set<UniversalGuiWrapper> childWrappers;
	private final Set<UniversalElement> childElements;

	public UniversalGuiWrapper(UniversalGuiWrapper parentWrapper)
	{
		this.parentWrapper = parentWrapper;
		this.childWrappers = new HashSet<UniversalGuiWrapper>();
		this.childElements = new HashSet<UniversalElement>();
	}

	public Collection<UniversalGuiWrapper> getChildWrappers()
	{
		return childWrappers;
	}
	
	public Collection<UniversalElement> getChildElements()
	{
		return childElements;
	}

    public void addElements(UniversalElement... elements)
    {
    	if (elements == null)
    	{
    		return;
    	}

    	for (UniversalElement elementI : elements)
    	{
    		this.childElements.add(elementI);
    	}
    }
    
    public void addWrappers(UniversalGuiWrapper... wrappers)
    {
    	if (wrappers == null)
    	{
    		return;
    	}

    	for (UniversalGuiWrapper wrapperI : wrappers)
    	{
    		childWrappers.add(wrapperI);
    	}
    }
    
    public UniversalGuiWrapper getParentWrapper()
    {
    	return parentWrapper;
    }
    
    public void setParentWrapper(UniversalGuiWrapper parentWrapper)
    {
    	this.parentWrapper = parentWrapper;
    }
}