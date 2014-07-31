package org.pikater.shared.experiment.universalformat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;
import org.pikater.core.ontology.subtrees.batchDescription.examples.SearchOnly;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.shared.XStreamHelper;
import org.pikater.shared.experiment.webformat.BoxType;

public class UniversalComputationDescription
{
	/**
	 * Priority of Batch.
	 */
	private int priority;

	/**
	 * Top-level options for this computation.
	 */
	private final Set<NewOption> globalOptions;
	
	/**
	 * Tree of ComputingDescription. Ontology elements wrapped in
	 * UniversalElement Set. Contains only FileDataSavers.
	 */
	private final Set<UniversalElement> rootElements;

	/**
	 * Contains all elements added to this computation.
	 */
	private final Set<UniversalElement> allElements;

	/**
	 * Default wrapper for top-level elements. All elements and wrappers either have this
	 * wrapper as a parent or another. 
	 */
	private final UniversalGuiWrapper defaultWrapper;
	
	public UniversalComputationDescription()
	{
		this.globalOptions = new HashSet<NewOption>();
		this.rootElements = new HashSet<UniversalElement>();
		this.allElements = new HashSet<UniversalElement>();
		this.defaultWrapper = new UniversalGuiWrapper(null);
	}
	
	// ----------------------------------------------------------
	// SOME BASIC INTERFACE

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public Set<NewOption> getGlobalOptions()
	{
		return globalOptions;
	}

	public void addGlobalOptions(NewOption... options)
	{
		this.globalOptions.addAll(Arrays.asList(options));
	}
	
	public void addGlobalOptions(Set<NewOption> options) {
		this.globalOptions.addAll(options);
	}
	
	public Set<UniversalElement> getRootElements()
	{
		return rootElements;
	}
	
	public Set<UniversalElement> getAllElements()
	{
		return allElements;
	}

	public void addElement(UniversalElement element)
	{
		if(!element.isOntologyDefined())
		{
			throw new IllegalArgumentException("The given element didn't have ontology defined.");
		}
		else
		{
			allElements.add(element);
			if (element.getOntologyInfo().getOntologyClass() == FileDataSaver.class)
			//if (BoxType.fromOntologyClass(element.getOntologyInfo().getOntologyClass()) == BoxType.OUTPUT)
			{
				rootElements.add(element);
			}
			// TODO: change data structures when wrappers are really needed
			/*
			if (element.getParentWrapper() == null)
			{
				defaultWrapper.addElements(element);
			}
			*/
		}
	}
	
	/**
	 * Can this experiment be shown in the experiment editor? In other words,
	 * can it be converted to the web format?
	 */
	public boolean isGUICompatible()
	{
		for (UniversalElement elementI : this.rootElements)
		{
			if (elementI.getGUIInfo() == null)
			{
				return false;
			}
		}
		return true;
	}
	
	// ----------------------------------------------------------
	// MANIPULATION WITH WRAPPERS

	/**
	 * Creates a new {@link UniversalGuiWrapper} and moves all the listed arguments inside it.
	 * REQUIREMENT: argument wrappers and elements must have identical direct parent ({@link UniversalGuiWrapper}).
	 */
	/*
	public void insertIntoGuiWrapper(ArrayList<UniversalGuiWrapper> wrappers, ArrayList<UniversalElement> elements)
	{
		if (((wrappers == null) || wrappers.isEmpty()) && ((elements == null) || elements.isEmpty()))
		{
			throw new IllegalArgumentException("No wrappers or elements to construct the new wrapper from were received.");
		}

		UniversalGuiWrapper parent = null;

		if (guiWrappers != null && (!guiWrappers.isEmpty()))
		{
			parent = this.getParentWrapperOf(guiWrappers.get(0));
		}
		else
		{
			parent = this.getParentWrapperOf(elements.get(0));
		}

		for (UniversalGuiWrapper wrapperI : guiWrappers)
		{
			UniversalGuiWrapper parentI = this.getParentWrapperOf(wrapperI);
			if (parentI != parent)
			{
				throw new IllegalArgumentException(
						"GuiWrappers and elements are not "
								+ "in the same UniversalGuiWrapper");
			}
		}

		for (UniversalElement elementI : elements)
		{
			UniversalGuiWrapper parentI = this.getParentWrapperOf(elementI);
			if (parentI != parent)
			{
				throw new IllegalArgumentException(
						"GuiWrappers and elements are not "
								+ "in the same UniversalGuiWrapper");
			}
		}

		UniversalGuiWrapper newWrapper = new UniversalGuiWrapper();
		newWrapper.addElements(elements);
		newWrapper.addWrappers(guiWrappers);

		parent.replaceByWrapper(newWrapper);
	}

    public void replaceByWrapper(UniversalGuiWrapper wrapper)
    {
    	childElements.removeAll(wrapper.getChildWrappers());
    	childWrappers.removeAll(wrapper.getChildWrappers());
    	childWrappers.add(wrapper);
    }
	
    // this has be implemented in a completely different way
    public UniversalGuiWrapper getParentWrapper(UniversalElement element) {

    	if (this.elements.contains(element)) {
    		return this;
    	} else {

    		for (UniversalGuiWrapper wrapperI : guiWrappers) {
    			UniversalGuiWrapper result =
    					wrapperI.getParentWrapper(element);
    			if ( result != null ) {
    				return result;
    			}
    		}
    		return null;
    	}
    }
	*/
	
	public static UniversalComputationDescription getDummy()
	{
		UniversalComputationDescription result = new UniversalComputationDescription();
		
		UniversalOntology ontologyInfo1 = new UniversalOntology();
		UniversalElement element1 = new UniversalElement();
		element1.setGUIInfo(new UniversalGui(10, 10));
		element1.setOntologyInfo(ontologyInfo1);
		
		UniversalOntology ontologyInfo2 = new UniversalOntology();
		UniversalElement element2 = new UniversalElement();
		element2.setGUIInfo(new UniversalGui(500, 10));
		element2.setOntologyInfo(ontologyInfo2);
		
		UniversalOntology ontologyInfo3 = new UniversalOntology();
		UniversalElement element3 = new UniversalElement();
		element3.setGUIInfo(new UniversalGui(400, 300));
		element3.setOntologyInfo(ontologyInfo3);
		
		result.addElement(element1);
		result.addElement(element2);
		result.addElement(element3);
		
		return result;
	}
	
	public String toXML()
	{
		return XStreamHelper.serializeToXML(this, 
        		XStreamHelper.getSerializerWithProcessedAnnotations(UniversalComputationDescription.class));
	}
	
	public static UniversalComputationDescription fromXML(String xml)
	{
		return XStreamHelper.deserializeFromXML(UniversalComputationDescription.class, xml, 
        		XStreamHelper.getSerializerWithProcessedAnnotations(UniversalComputationDescription.class));
	}
	
	public static void main(String[] args)
	{
		UniversalComputationDescription uDescription = SearchOnly.createDescription().exportUniversalComputationDescription();
		System.out.println(XStreamHelper.serializeToXML(uDescription, 
				XStreamHelper.getSerializerWithProcessedAnnotations(UniversalComputationDescription.class)));
	}
}