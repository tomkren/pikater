package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.shared.experiment.webformat.BoxInfoCollection;

import com.vaadin.shared.communication.ClientRpc;

public interface MainUIExtensionClientRpc extends ClientRpc
{
	void setBoxDefinitions(BoxInfoCollection boxDefinitions);
}