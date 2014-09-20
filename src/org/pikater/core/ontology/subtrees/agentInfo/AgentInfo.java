package org.pikater.core.ontology.subtrees.agentInfo;

import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.shared.util.ICloneable;
import org.pikater.shared.util.collections.CollectionUtils;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class AgentInfo implements Concept, ICloneable
{
	private static final long serialVersionUID = 5587052921037796722L;

	private String agentClassName;
	private String ontologyClassName;
	
	private String name;
	private String description;

	private List<Slot> inputSlots;
	private List<Slot> outputSlots;
	
	private NewOptions options;

	public AgentInfo() {
		this.inputSlots = new ArrayList<Slot>();
		this.outputSlots = new ArrayList<Slot>();
		this.options = new NewOptions();
	}

	//---------------------------------------------------------
	// CUSTOM INSTANCE COMPARING - GENERATED WITH ECLIPSE
	
	/*
	 * Only compare the {@link #agentClassName} and {@link #ontologyClassName}
	 * fields.
	 * Web package (namely {@link AgentInfoCollection} depends on this.
	 */
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((agentClassName == null) ? 0 : agentClassName.hashCode());
		result = prime
				* result
				+ ((ontologyClassName == null) ? 0 : ontologyClassName
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgentInfo other = (AgentInfo) obj;
		if (agentClassName == null)
		{
			if (other.agentClassName != null)
				return false;
		}
		else if (!agentClassName.equals(other.agentClassName))
			return false;
		if (ontologyClassName == null)
		{
			if (other.ontologyClassName != null)
				return false;
		}
		else if (!ontologyClassName.equals(other.ontologyClassName))
			return false;
		return true;
	}
	
	//---------------------------------------------------------
	// REQUIRED GETTERS/SETTERS
	
	public String getAgentClassName() {
		return agentClassName;
	}
	public void setAgentClassName(String agentClassName) {
		this.agentClassName = agentClassName;
	}
	public String getOntologyClassName() {
		return ontologyClassName;
	}
	public void setOntologyClassName(String ontologyClassName) {
		this.ontologyClassName = ontologyClassName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Slot> getInputSlots() {
		return inputSlots;
	}
	public void setInputSlots(List<Slot> inputSlots) {
		this.inputSlots = inputSlots;
	}
	public List<Slot> getOutputSlots() {
		return outputSlots;
	}
	public void setOutputSlots(List<Slot> outputSlots) {
		this.outputSlots = outputSlots;
	}
	public NewOptions getOptions() {
		return options;
	}
	public void setOptions(NewOptions options) {
		this.options = options;
	}
	
	//---------------------------------------------------------
	// OTHER INTERFACE
	
	@Override
	public AgentInfo clone()
	{
		AgentInfo result;
		try
		{
			result = (AgentInfo) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		result.setOntologyClassName(ontologyClassName);
		result.setAgentClassName(agentClassName);
		result.setName(name);
		result.setDescription(description);
		result.setInputSlots(CollectionUtils.deepCopy(inputSlots));
		result.setInputSlots(CollectionUtils.deepCopy(outputSlots));
		result.setOptions(options.clone());
		return result;
	}

	public void importAgentClass(Class<?> agentClass) {
		this.agentClassName = agentClass.getName();
	}

	public void importOntologyClass(Class<?> ontologyClass) {
		this.ontologyClassName = ontologyClass.getName();
	}

	public void addOption(NewOption option) {
		if (option == null) {
			throw new IllegalArgumentException("Argument option can't be null");
		}
		this.options.addOption(option);
	}
	public void addOptions(List<NewOption> options) {
		if (options == null) {
			throw new IllegalArgumentException("Argument options can't be null");
		}
		this.options.addOptions(options);
	}
	
	public void addInputSlot(Slot inputSlot) {
		if (inputSlot == null) {
			throw new IllegalArgumentException("Argument inputSlot can't be null");
		}
		this.inputSlots.add(inputSlot);
	}
	public void addOutputSlot(Slot outputSlot) {
		if (outputSlot == null) {
			throw new IllegalArgumentException("Argument outputSlot can't be null");
		}
		this.outputSlots.add(outputSlot);
	}
	
	public Slot fetchInputSlotByName(String slotName)
	{
		for(Slot slot : inputSlots)
		{
			if(slot.getName().equals(slotName))
			{
				return slot;
			}
		}
		return null;
	}
	
	public Slot fetchOutputSlotByName(String slotName)
	{
		for(Slot slot : outputSlots)
		{
			if(slot.getName().equals(slotName))
			{
				return slot;
			}
		}
		return null;
	}
	
	public boolean isIdentifiedBy(String ontologyClassName, String agentClassName)
	{
		if(this.agentClassName == null) // some kind of a weird agent (should be exactly one)
		{
			if(this.ontologyClassName.equals(ontologyClassName) && agentClassName == null)
			{
				return true;
			}
		}
		else // just what we expect, the good option
		{
			if(this.ontologyClassName.equals(ontologyClassName) && this.agentClassName.equals(agentClassName))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isOntologyType(Class<?> ontologyClass) {
		
		return this.ontologyClassName.equals(ontologyClass.getName());
	}
	
	public String exportXML() {
		XStream xstream = new XStream();
		return xstream.toXML(this);
	}

	public static AgentInfo importXML(String xmlContent) {
		XStream xstream = new XStream();

		Scanner scanner = new Scanner(xmlContent);
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		return (AgentInfo)xstream.fromXML(xml);
	}
	
}
