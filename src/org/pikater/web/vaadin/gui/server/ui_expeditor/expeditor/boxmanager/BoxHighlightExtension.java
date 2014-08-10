package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager;

import org.pikater.web.vaadin.gui.client.extensions.BoxHighlightExtensionSharedState;
import org.pikater.web.vaadin.gui.server.components.flowlayout.HorizontalFlowLayout;

import com.vaadin.server.AbstractExtension;

public class BoxHighlightExtension extends AbstractExtension
{
	private static final long serialVersionUID = 3081925647057133214L;

	public BoxHighlightExtension(String kineticConnectorID, Integer[] boxesToBeHighlighted)
	{
		/*
		registerRpc(new BoxHighlightExtensionServerRpc()
		{
		});
		*/
		
		getState().kineticConnectorID = kineticConnectorID;
		getState().boxesToBeHighlighted = boxesToBeHighlighted;
	}
	
	@Override
	protected BoxHighlightExtensionSharedState getState()
	{
		return (BoxHighlightExtensionSharedState) super.getState();
	}
	
	/**
	 * Exposing the inherited API.
	 * @param mainUI
	 */
	public void extend(HorizontalFlowLayout component)
    {
        super.extend(component);
    }
}