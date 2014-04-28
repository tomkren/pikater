package org.pikater.web.vaadin.gui;

import org.pikater.web.vaadin.gui.client.kineticeditor.KineticEditorServerRpc;
import org.pikater.web.vaadin.gui.client.kineticeditor.KineticEditorState;

import com.vaadin.ui.AbstractComponent;

public class KineticEditor extends AbstractComponent
{
	private final KineticEditorServerRpc rpc = new KineticEditorServerRpc()
	{
	};

	public KineticEditor()
	{
		registerRpc(rpc);
	}

	@Override
	public KineticEditorState getState()
	{
		return (KineticEditorState) super.getState();
	}
}
