package org.pikater.web.vaadin.gui.client.linklabel;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

public interface LinkLabelServerRpc extends ServerRpc
{
	public void clicked(MouseEventDetails mouseDetails);
}
