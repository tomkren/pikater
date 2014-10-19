package org.pikater.core.ontology.subtrees.model;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;

import jade.content.Concept;

public class Models implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4746321914578465717L;

	private List<Model> models;

	public Models() {
		this.models = new ArrayList<Model>(); 
	}

	public List<Model> getModels() {
		return models;
	}
	public void setModels(List<Model> models) {
		this.models = models;
	}
	public void addModel(Model model) {
		this.models.add(model);
	}
	public void addAllModels(List<Model> models) {
		this.models.addAll(models);
	}

	public List<Model> getModelsByAgentType(Class<?> agentType) {
		
		List<Model> selectedModels = new ArrayList<Model>();
		for (Model modelI : models) {
			if (modelI.isAgentType(agentType)) {
				selectedModels.add(modelI);
			}
		}
		
		return selectedModels;
	}

	public List<IValueData> getModelIDsByAgentType(Class<?> agentType) {
		
		List<Model> selectedModels = getModelsByAgentType(agentType);
		
		List<IValueData> modelIDs = new ArrayList<IValueData>();
		
		for (Model modelI : selectedModels) {
			modelIDs.add(new IntegerValue(modelI.getResultID()));
		}
		
		return modelIDs;
	}

}
