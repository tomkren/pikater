package org.pikater.shared.experiment.universalformat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;
import org.pikater.core.ontology.subtrees.batchDescription.examples.SearchOnly;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.shared.XStreamHelper;
import org.pikater.shared.util.SimpleIDGenerator;

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
	 * ID generator for {@link UniversalOntology} instances within {@link UniversalElement}.
	 */
	private final SimpleIDGenerator idGenerator;

	public UniversalComputationDescription()
	{
		this.globalOptions = new HashSet<NewOption>();
		this.rootElements = new HashSet<UniversalElement>();
		this.allElements = new HashSet<UniversalElement>();
		this.idGenerator = new SimpleIDGenerator();
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
			element.getOntologyInfo().setId(idGenerator.getAndIncrement());
			
			//if (BoxType.fromOntologyClass(element.getOntologyInfo().getOntologyClass()) == BoxType.OUTPUT)
			if (element.getOntologyInfo().getOntologyClass().equals(FileDataSaver.class))
			{
				rootElements.add(element);
			}
			allElements.add(element);
		}
	}
	
	/**
	 * Can this experiment be shown in the experiment editor? In other words,
	 * can it be converted to the web format?
	 */
	public boolean isGUICompatible()
	{
		for (UniversalElement elementI : this.allElements)
		{
			if (elementI.getGuiInfo() == null)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Determines whether this experiment is ready to be scheduled and should not
	 * end up with needless errors.
	 * @return
	 */
	public boolean isValid()
	{
		// TODO: all kinds of consistency checks
		// TODO: connector.validate()
		return true;
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