package org.pikater.web.vaadin.gui.client.kineticcomponent;

import com.vaadin.shared.communication.ServerRpc;

public interface KineticComponentServerRpc extends ServerRpc
{
	void setSchemaModified(boolean modified);
}
