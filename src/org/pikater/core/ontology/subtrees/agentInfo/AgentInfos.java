package org.pikater.core.ontology.subtrees.agentInfo;

import java.util.ArrayList;
import java.util.List;

import org.jfree.util.Log;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.model.Models;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.OptionList;
import org.pikater.core.ontology.subtrees.newOption.Value;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.ITypedValue;

import jade.content.Concept;

public class AgentInfos implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4635414687017805968L;

	private List<AgentInfo> agentInfos;

	public List<AgentInfo> getAgentInfos() {
		if (this.agentInfos == null) {
			return new ArrayList<AgentInfo>();
		}
		return agentInfos;
	}

	public void setAgentInfos(List<AgentInfo> agentInfos) {
		this.agentInfos = agentInfos;
	}

	public void addAgentInfo(AgentInfo agentInfo) {
		if (this.agentInfos == null) {
			this.agentInfos = new ArrayList<AgentInfo>();
		}
		this.agentInfos.add(agentInfo);
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
			
			List<ITypedValue> typedValues = models.getModelIDsByAgentType(agentClass);
			
			OptionList options = agentInfoI.getOptions();
			NewOption optionModel = options.getOptionByName("model");
			Value value = optionModel.convertToSingleValue();
			SetRestriction setRestriction = value.getType().getSetRestriction();
			setRestriction.addAllValues(typedValues);
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
