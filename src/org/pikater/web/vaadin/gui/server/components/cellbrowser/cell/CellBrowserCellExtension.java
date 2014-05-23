package org.pikater.web.vaadin.gui.server.components.cellbrowser.cell;

import org.pikater.web.vaadin.gui.client.extensions.CellBrowserCellExtensionClientRpc;
import org.pikater.web.vaadin.gui.client.extensions.CellBrowserCellExtensionSharedState;

import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.CustomLayout;

public class CellBrowserCellExtension extends AbstractExtension implements CellBrowserCellExtensionClientRpc
{
	private static final long serialVersionUID = 5856285176256055275L;

	public CellBrowserCellExtension(String selectionClassName)
	{
		super();
		
		getState().selectionClassName = selectionClassName;
	}
	
	/**
	 * Exposing the inherited API for extension.
	 * @param mainUI
	 */
	public void extend(CustomLayout innerCellLayout)
    {
        super.extend(innerCellLayout);
    }
	
	@Override
	protected CellBrowserCellExtensionSharedState getState()
	{
		return (CellBrowserCellExtensionSharedState) super.getState();
	}

	// ------------------------------------------------------------
	// THE IMPORTANT STUFF
	
	@Override
	public void select()
	{
		getRpcProxy(CellBrowserCellExtensionClientRpc.class).select();
	}

	@Override
	public void deselect()
	{
		getRpcProxy(CellBrowserCellExtensionClientRpc.class).deselect();
	}
}
