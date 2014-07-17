package org.pikater.web.vaadin.gui.client.anchor;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

public interface AnchorServerRpc extends ServerRpc
{
	public void clicked(MouseEventDetails mouseDetails);
}