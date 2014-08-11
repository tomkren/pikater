package org.pikater.web.vaadin.gui.shared.kineticcomponent;

public interface IKineticComponent
{
	void highlightBoxes(Integer[] boxIDs);
	void cancelBoxHighlight();
	void cancelSelection();
	void resetEnvironment();
}