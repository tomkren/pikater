package org.pikater.shared.database.experiment;

public class UniversalElementWrapper {

	private UniversalElement element;
	private UniversalGui gui = new UniversalGui();


	public UniversalElement getElement() {
		return element;
	}
	public void setElement(UniversalElement element) {
		this.element = element;
	}

	public UniversalGui getGui() {
		return gui;
	}
	public void setGui(UniversalGui gui) {
		this.gui = gui;
	}

}
