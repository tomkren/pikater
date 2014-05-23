package org.pikater.web.vaadin.gui.server.components.kineticcomponent;

import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentClientRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentServerRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentState;
import org.pikater.web.vaadin.gui.server.components.experimenteditor.ExperimentEditor;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractComponent;

@JavaScript(value = "kinetic-v4.7.3-dev.js")
public class KineticComponent extends AbstractComponent
{
	private static final long serialVersionUID = -539901377528727478L;
	
	/**
	 * Constant reference to the parent editor component.
	 */
	private final ExperimentEditor parentEditor;
	
	/*
	 * Dynamic information from the client side.
	 */
	private boolean contentModified;
	private int absoluteLeft;
	private int absoluteTop;
	
	public KineticComponent(ExperimentEditor parentEditor)
	{
		super();
		setSizeFull();
		
		this.parentEditor = parentEditor;
		
		this.contentModified = false;
		this.absoluteLeft = 0;
		this.absoluteTop = 0;
		
		// first define server RPC
		registerRpc(new KineticComponentServerRpc()
		{
			private static final long serialVersionUID = -2769231541745495584L;

			@Override
			public void setSchemaModified(boolean modified)
			{
				contentModified = modified;
				
				// TODO: modify the editor's tabsheet's tab
			}

			@Override
			public void onLoadCallback(int absoluteX, int absoluteY)
			{
				KineticComponent.this.absoluteLeft = absoluteX;
				KineticComponent.this.absoluteTop = absoluteY;
			}
		});
	}
	
	public boolean isContentModified()
	{
		return contentModified;
	}
	
	public int toRelativeLeft(int posX)
	{
		return posX - absoluteLeft;
	}
	
	public int toRelativeTop(int posY)
	{
		return posY - absoluteTop;
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
