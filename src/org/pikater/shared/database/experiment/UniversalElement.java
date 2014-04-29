package org.pikater.shared.database.experiment;


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
		
		if (element == null) {
			throw new IllegalStateException("UniversalElement cann't be null");
		}
		
		this.element = element;

		this.uModel.addElement(this);

	}

	public UniversalGui getGui() {
		return gui;
	}
	public void setGui(UniversalGui gui) {

		if (this.gui != null && gui == null) {
			throw new IllegalStateException("UniversalGui cann't be changed to null");
		}

		this.gui = gui;
	}

}
