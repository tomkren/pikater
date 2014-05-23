package org.pikater.web.vaadin.gui.client.extensions;

import com.vaadin.shared.communication.ClientRpc;

public interface CellBrowserCellExtensionClientRpc extends ClientRpc
{
	void select();
	void deselect();
}
