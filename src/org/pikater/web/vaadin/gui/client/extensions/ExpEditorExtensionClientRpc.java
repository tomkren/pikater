package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.shared.experiment.webformat.BoxInfoCollection;

import com.vaadin.shared.communication.ClientRpc;

public interface ExpEditorExtensionClientRpc extends ClientRpc
{
	void command_setBoxDefinitions(BoxInfoCollection boxDefinitions);
	void command_setBoxSize(int percent);
}