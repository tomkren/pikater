package org.pikater.web.vaadin.gui.client.extensions;

import com.vaadin.shared.communication.ClientRpc;

public interface ExpEditorExtensionClientRpc extends ClientRpc
{
	void command_setBoxSize(int percent);
}