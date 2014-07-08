package org.pikater.core.ontology.subtrees.model;

import java.util.List;

import jade.content.Concept;

public class Models implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4746321914578465717L;

	private List<Model> models;

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

}
