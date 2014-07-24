package org.pikater.core.ontology.subtrees.agentInfo;

import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

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

	private NewOptions options;
	
	private List<Slot> inputSlots;
	private List<Slot> outputSlots;

	public String getAgentClassName() {
		return agentClassName;
	}
	public void setAgentClassName(String agentClassName) {
		this.agentClassName = agentClassName;
	}
	public void importAgentClass(Class<?> agentClass) {
		this.agentClassName = agentClass.getName();
	}

	public String getOntologyClassName() {
		return ontologyClassName;
	}
	public void setOntologyClassName(String ontologyClassName) {
		this.ontologyClassName = ontologyClassName;
	}
	public void importOntologyClass(Class<?> ontologyClass) {
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

	public NewOptions getOptions()
	{
		return options;
	}
	public void setOptions(NewOptions options)
	{
		this.options = options;
	}
	public void addOption(NewOption option) {
		if (option == null) {
			throw new IllegalArgumentException("Argument option cann't be null");
		}
		this.options.addOption(option);
	}
	public void addOptions(List<NewOption> options) {
		if (options == null) {
			throw new IllegalArgumentException("Argument options cann't be null");
		}
		this.options.addOptions(options);
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
