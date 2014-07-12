package org.pikater.core.ontology.subtrees.agentInfo;

import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.OptionList;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class AgentInfo implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5587052921037796722L;

	private String agentClassName;
	private String ontologyClassName;
	
	private String name;
	private String description;

	private OptionList options;
	
	private List<Slot> inputSlots;
	private List<Slot> outputSlots;

	public String getAgentClassName() {
		return agentClassName;
	}
	public void setAgentClassName(String agentClassName) {
		this.agentClassName = agentClassName;
	}
	public void setAgentClass(Class<?> agentClass) {
		this.agentClassName = agentClass.getName();
	}

	public String getOntologyClassName() {
		return ontologyClassName;
	}
	public void setOntologyClassName(String ontologyClassName) {
		this.ontologyClassName = ontologyClassName;
	}
	public void setOntologyClass(Class<?> ontologyClass) {
		this.ontologyClassName = ontologyClass.getName();
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

	public OptionList getOptions()
	{
		return options;
	}
	public void setOptions(OptionList options)
	{
		this.options = options;
	}
	public void addOption(NewOption option) {
		if (this.options == null) {
			this.options = new OptionList();
		}
		this.options.addOption(option);
	}

	public List<Slot> getInputSlots() {
		return inputSlots;
	}
	public void setInputSlots(List<Slot> inputSlots) {
		this.inputSlots = inputSlots;
	}
	public void addInputSlot(Slot inputSlot) {
		if (this.inputSlots == null) {
			this.inputSlots = new ArrayList<Slot>();
		}
		this.inputSlots.add(inputSlot);
	}

	public List<Slot> getOutputSlots() {
		return outputSlots;
	}
	public void setOutputSlots(List<Slot> outputSlots) {
		this.outputSlots = outputSlots;
	}
	public void addOutputSlot(Slot outputSlot) {
		if (this.outputSlots == null) {
			this.outputSlots = new ArrayList<Slot>();
		}
		this.outputSlots.add(outputSlot);
	}
	
	public boolean isOntologyType(Class<?> ontologyClass) {
		
		return this.ontologyClassName.equals(ontologyClass.getName());
	}
	
	public boolean equals(AgentInfo agentInfo) {
		
		return this.getAgentClassName().equals(agentInfo.getAgentClassName());
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
		
		AgentInfo agentInfo =
				(AgentInfo)xstream.fromXML(xml);
		
		return agentInfo;
	}
	
	
}
