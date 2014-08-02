package org.pikater.core.ontology.subtrees.agentInfo;

import java.util.ArrayList;
import java.util.List;

import org.jfree.util.Log;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.model.Models;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

import jade.content.Concept;

public class AgentInfos implements Concept {

	/**
	 * 
	 */
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

	public boolean contains(Class<? extends Agent_AbstractExperiment> agent) {
		for (AgentInfo agentInfoI : this.agentInfos) {
			
			if ( agentInfoI.getAgentClassName().equals(agent.getName()) ) {
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
			NewOption optionModel = options.getOptionByName("model");
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
