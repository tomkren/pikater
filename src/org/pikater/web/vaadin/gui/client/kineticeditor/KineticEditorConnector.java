package org.pikater.web.vaadin.gui.client.kineticeditor;

import com.google.gwt.core.client.GWT;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.client.communication.RpcProxy;

import org.pikater.web.vaadin.gui.KineticEditor;

import com.vaadin.client.communication.StateChangeEvent;

@Connect(KineticEditor.class)
public class KineticEditorConnector extends AbstractComponentConnector
{
	private final KineticEditorServerRpc serverRPC = RpcProxy.create(KineticEditorServerRpc.class, this);

	public KineticEditorConnector()
	{
		registerRpc(KineticEditorClientRpc.class, new KineticEditorClientRpc()
		{
		});
	}

	@Override
	protected KineticEditorWidget createWidget()
	{
		KineticEditorWidget result = GWT.create(KineticEditorWidget.class);
		result.setConnector(serverRPC);
		return result;
	}

	@Override
	public KineticEditorWidget getWidget()
	{
		return (KineticEditorWidget) super.getWidget();
	}

	@Override
	public KineticEditorState getState()
	{
		return (KineticEditorState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent)
	{
		super.onStateChanged(stateChangeEvent);
	}

}
