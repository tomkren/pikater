package org.pikater.web.vaadin.gui.client.kineticcomponent;

import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.client.communication.RpcProxy;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.SchemaDataSource;
import org.pikater.web.vaadin.gui.server.components.experimenteditor.KineticComponent;

import com.vaadin.client.communication.StateChangeEvent;

@Connect(KineticComponent.class)
public class KineticComponentConnector extends AbstractComponentConnector
{
	private static final long serialVersionUID = 3442248033567337353L;
	
	public final KineticComponentServerRpc serverRPC = RpcProxy.create(KineticComponentServerRpc.class, this);
	
	/**
	 * Called first when creating the connector.
	 */
	public KineticComponentConnector()
	{
		/*
		 * NOTE: calling the server RPC in the constructor will cause a null pointer exception. Rather use the widget's
		 * onLoad method for some further initializations if shared state is not suitable. 
		 */
		registerRpc(KineticComponentClientRpc.class, new KineticComponentClientRpc()
		{
			private static final long serialVersionUID = -263115608289713347L;

			@Override
			public void createBox(BoxInfo info, int posX, int posY)
			{
				getWidget().createBox(info, posX, posY);
			}

			@Override
			public void loadExperiment(SchemaDataSource experiment)
			{
				getWidget().loadExperiment(experiment);
			}

			@Override
			public void resetKineticEnvironment()
			{
				getWidget().resetKineticEnvironment();
			}
		});
	}
	
	// --------------------------------------------------------------------
	// METHODS INHERITED FROM CONNECTOR

	/**
	 * Called second when creating the connector.
	 */
	@Override
	protected KineticComponentWidget createWidget()
	{
		return new KineticComponentWidget(this);
	}

	@Override
	public KineticComponentWidget getWidget()
	{
		return (KineticComponentWidget) super.getWidget();
	}

	@Override
	public KineticComponentState getState()
	{
		return (KineticComponentState) super.getState();
	}

	/**
	 * Called third when creating the connector.
	 */
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent)
	{
		super.onStateChanged(stateChangeEvent);
	}
}
