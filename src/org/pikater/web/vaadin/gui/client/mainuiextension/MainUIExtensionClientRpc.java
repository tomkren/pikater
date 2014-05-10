package org.pikater.web.vaadin.gui.client.mainuiextension;

import org.pikater.shared.experiment.webformat.BoxInfoCollection;

import com.vaadin.shared.communication.ClientRpc;

public interface MainUIExtensionClientRpc extends ClientRpc
{
	void setBoxDefinitions(BoxInfoCollection boxDefinitions);
}