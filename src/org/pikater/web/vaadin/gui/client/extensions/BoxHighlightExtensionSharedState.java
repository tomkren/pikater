package org.pikater.web.vaadin.gui.client.extensions;

import com.vaadin.shared.communication.SharedState;

public class BoxHighlightExtensionSharedState extends SharedState
{
	private static final long serialVersionUID = -8009071309826997143L;
	
	public String kineticConnectorID = null;
	public Integer[] boxesToBeHighlighted = null;
}