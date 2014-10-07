package org.pikater.core.ontology.subtrees.agentInfo;

import java.util.ArrayList;
import java.util.List;

import org.jfree.util.Log;
import org.pikater.core.agents.experiment.virtual.Agent_VirtualNotSpecifiedComputingAgent;
import org.pikater.core.ontology.subtrees.agent.AgentClass;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.model.Models;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

import jade.content.Concept;

/**
 * Ontology contains {@link List<AgentInfo>},
 * list of informations about PikaterAgents
 * 
 */
public class AgentInfos implements Concept {

	private static final long serialVersionUID = 4635414687017805968L;

	private List<AgentInfo> agentInfos;

	public AgentInfos() {
		agentInfos = new ArrayList<AgentInfo>();
	}
	
	public List<AgentInfo> getAgentInfos() {
		return agentInfos;
	}

	public void setAgentInfos(List<AgentInfo> agentInfos) {
		if (agentInfos == null) {
			throw new IllegalArgumentException("Argument agentInfos can't be null");
		}

		this.agentInfos = agentInfos;
	}

	public void addAgentInfo(AgentInfo agentInfo) {
		if (agentInfo == null) {
			throw new IllegalArgumentException("Argument agentInfo can't be null");
		}
		this.agentInfos.add(agentInfo);
	}
	public void addAgentInfo(List<AgentInfo> agentInfos) {
		if (agentInfos == null) {
			throw new IllegalArgumentException("Argument agentInfos can't be null");
		}
		this.agentInfos.addAll(agentInfos);
	}
	public boolean contains(AgentClass agentClass) {
		if (agentClass == null) {
			throw new IllegalArgumentException("Argument agentClass can't be null");
		}
		
		for (AgentInfo agentInfoI : this.agentInfos) {
			
			String agentClassNameI = agentInfoI.getAgentClassName();
			if (agentClassNameI == null) {
				if (agentClass.getAgentClass().equals(
						Agent_VirtualNotSpecifiedComputingAgent.class.getName())) {
					return true;
				}
			} else if ( agentClassNameI.equals(agentClass.getAgentClass()) ) {
				return true;
			}
		}
		
		return false;
	}
	
	public void importModels(Models models) {
		
		for (AgentInfo agentInfoI: getAllCAAgentInfos()) {
			String agentClassName = agentInfoI.getAgentClassName();
			Class<?> agentClass = null;
			try {
				agentClass = Class.forName(agentClassName);
			} catch (ClassNotFoundException e) {
				Log.error(e.getMessage(), e);
			}
			
			List<IValueData> typedValues = models.getModelIDsByAgentType(agentClass);
			
			NewOptions options = agentInfoI.getOptions();
			NewOption optionModel = options.fetchOptionByName("model");
			Value value = optionModel.toSingleValue();
			SetRestriction setRestriction = value.getType().getSetRestriction();
			setRestriction.addValues(typedValues);
		}
	}
	
	private List<AgentInfo> getAllCAAgentInfos() {
		
		List<AgentInfo> selectedInfos =
				new ArrayList<AgentInfo>();
		for (AgentInfo angentInfoI : agentInfos) {
			
			if (angentInfoI.isOntologyType(ComputingAgent.class)) {
				selectedInfos.add(angentInfoI);
			}
		}
		
		return selectedInfos;
	}
	
}
