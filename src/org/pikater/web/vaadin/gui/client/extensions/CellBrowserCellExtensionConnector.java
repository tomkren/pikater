package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.web.vaadin.gui.server.layouts.cellbrowser.cell.CellBrowserCellExtension;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CellBrowserCellExtension.class)
public class CellBrowserCellExtensionConnector extends AbstractExtensionConnector
{
	private static final long serialVersionUID = 8605177733055933992L;
	
	@Override
	protected void extend(ServerConnector target)
	{
		final Widget extendedWidget = ((ComponentConnector) target).getWidget();
		
		registerRpc(CellBrowserCellExtensionClientRpc.class, new CellBrowserCellExtensionClientRpc()
		{
			private static final long serialVersionUID = -8805636808343042478L;
			
			@Override
			public void select()
			{
				// make sure to fetch the element here - it is not yet constructed when the "extend" method is called
				Element innerTable = extendedWidget.getElement().getFirstChildElement();
				innerTable.addClassName(getState().selectionClassName);
			}

			@Override
			public void deselect()
			{
				// make sure to fetch the element here - it is not yet constructed when the "extend" method is called
				Element innerTable = extendedWidget.getElement().getFirstChildElement();
				innerTable.removeClassName(getState().selectionClassName);
			}
		});
	}
	
	@Override
	public CellBrowserCellExtensionSharedState getState()
	{
		return (CellBrowserCellExtensionSharedState) super.getState();
	}
}
