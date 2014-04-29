package org.pikater.shared.database.experiment;

import org.pikater.core.ontology.description.FileDataSaver;

public class UniversalElement {

	private UniversalComputationDescription uModel;
	private UniversalOntology element;
	private UniversalGui gui;


	@SuppressWarnings("all")
	private UniversalElement() {}

	public UniversalElement(
			UniversalComputationDescription description) {
		this.uModel = description;
	}

	
	public UniversalOntology getElement() {
		return element;
	}
	public void setElement(UniversalOntology element) {
		this.element = element;
		
		this.uModel.addElement(this);
		
		if (element.getType() == FileDataSaver.class) {
			this.uModel.addRootElement(this);
		}
	}

	public UniversalGui getGui() {
		return gui;
	}
	public void setGui(UniversalGui gui) {
		this.gui = gui;
	}

}
