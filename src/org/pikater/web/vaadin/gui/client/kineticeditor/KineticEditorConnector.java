package org.pikater.web.vaadin.gui.client.kineticeditor;

import com.google.gwt.core.client.GWT;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.client.communication.RpcProxy;

import org.pikater.web.vaadin.gui.server.components.KineticEditor;

import com.vaadin.client.communication.StateChangeEvent;

@Connect(KineticEditor.class)
public class KineticEditorConnector extends AbstractComponentConnector
{
	private static final long serialVersionUID = 3442248033567337353L;
	
	private final KineticEditorServerRpc serverRPC = RpcProxy.create(KineticEditorServerRpc.class, this);
	
	/**
	 * Called first when creating the connector.
	 */
	public KineticEditorConnector()
	{
		/*
		 * NOTE: calling the server RPC in the constructor will cause a null pointer exception. Rather use the widget's
		 * onLoad method for some further initializations if shared state is not suitable. 
		 */
		
		registerRpc(KineticEditorClientRpc.class, new KineticEditorClientRpc()
		{
			private static final long serialVersionUID = -263115608289713347L;
		});
	}

	/**
	 * Called second when creating the connector.
	 */
	@Override
	protected KineticEditorWidget createWidget()
	{
		KineticEditorWidget result = GWT.create(KineticEditorWidget.class);
		result.setServerRPC(serverRPC);
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

	/**
	 * Called third when creating the connector.
	 */
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent)
	{
		super.onStateChanged(stateChangeEvent);
	
		getWidget().loadExperiment(getState().experimentToLoad);
	}
}
