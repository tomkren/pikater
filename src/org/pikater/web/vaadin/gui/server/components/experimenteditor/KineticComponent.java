package org.pikater.web.vaadin.gui.server.components.experimenteditor;

import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentClientRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentServerRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentState;

import com.vaadin.ui.AbstractComponent;

public class KineticComponent extends AbstractComponent
{
	private static final long serialVersionUID = -539901377528727478L;
	
	private final ExperimentEditor parentEditor;
	
	public KineticComponent(ExperimentEditor parentEditor)
	{
		super();
		setStyleName("experiment-editor-kinetic-container");
		
		this.parentEditor = parentEditor;
		
		// first define server RPC
		registerRpc(new KineticComponentServerRpc()
		{
			private static final long serialVersionUID = -2769231541745495584L;

			@Override
			public void setSchemaModified(boolean modified)
			{
				// TODO Auto-generated method stub
			}
		});
	}
	
	public KineticComponentClientRpc getClientRPC()
	{
		return getRpcProxy(KineticComponentClientRpc.class);
	}
	
	@Override
	public KineticComponentState getState()
	{
		return (KineticComponentState) super.getState();
	}
}
