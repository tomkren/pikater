package org.pikater.web.vaadin.gui.client.kineticcomponent;

import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.client.communication.RpcProxy;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.ExperimentGraph;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.KineticComponent;

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
			
			/*
			 * NOTE: even though references to (for example) kinetic engine are available from this connector's widget,
			 * always call the widget from the methods here. The widget is meant to encapsulate the kinetic environment
			 * from the server's point of view. 
			 */
			
			@Override
			public void command_createBox(BoxInfo info)
			{
				getWidget().command_createBox(info);
			}

			@Override
			public void command_receiveExperimentToLoad(ExperimentGraph experiment)
			{
				getWidget().command_receiveExperimentToLoad(experiment);
			}

			@Override
			public void command_resetKineticEnvironment()
			{
				getWidget().command_resetKineticEnvironment();
			}

			@Override
			public void request_sendExperimentToSave()
			{
				getWidget().request_sendExperimentToSave();
			}

			@Override
			public void request_reloadVisualStyle()
			{
				getWidget().request_reloadVisualStyle();
			}
		});
	}
	
	// ----------------------------------------------------------------------------------------------
	// METHODS TO PRESERVE THE USER'S KINETIC CONTENT AGAINST VAADIN'S DETACH/VISIBILITY MECHANISMS.
	
	/*
	 * NOTE:
	 * Vaadin will destroy the connector when the server-side component gets detached from DOM (for instance when
	 * another tab gets selected) or set to invisible. Once attached again, Vaadin will recreate the client widget
	 * using the shared state.
	 * Ergo, if we want to preserve the widget's client state (Kinetic environment) without serializing to server,
	 * we have to:
	 * 1) serialize it locally, when the connector is unregistered,
	 * 2) deserialize it again, when the connector is recreated.
	 * That's what the "onUnregister()" and "init()" methods, respectively, do.
	 */
	
	@Override
	protected void init()
	{
		super.init();
		
		getWidget().command_receiveExperimentToLoad(KineticComponentRegistrar.getSavedExperimentFor(getConnectorId()));
	}
	
	@Override
	public void onUnregister()
	{
		super.onUnregister();
		
		KineticComponentRegistrar.saveExperimentFor(getConnectorId(), getWidget().getEngine().toIntermediateFormat());
	}
	
	// ----------------------------------------------------------------------------------------------
	// OTHER IMPORTANT METHODS INHERITED FROM THE CONNECTOR

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
