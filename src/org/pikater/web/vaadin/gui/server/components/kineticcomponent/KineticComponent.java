package org.pikater.web.vaadin.gui.server.components.kineticcomponent;

import org.pikater.shared.experiment.webformat.Experiment;
import org.pikater.shared.experiment.webformat.ExperimentMetadata;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentClientRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentServerRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentState;
import org.pikater.web.vaadin.gui.server.components.experimenteditor.CustomTabSheetTabComponent;
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
	
	/**
	 * Reference to the experiment editor tab linked to this content component.
	 */
	private CustomTabSheetTabComponent parentTab;
	
	/*
	 * Dynamic information from the client side.
	 */
	private int absoluteLeft;
	private int absoluteTop;
	
	public KineticComponent(ExperimentEditor parentEditor)
	{
		super();
		setSizeFull();
		
		this.parentEditor = parentEditor;
		
		this.absoluteLeft = 0;
		this.absoluteTop = 0;
		
		registerRpc(new KineticComponentServerRpc()
		{
			private static final long serialVersionUID = -2769231541745495584L;

			@Override
			public void command_setExperimentModified(boolean modified)
			{
				getState().serverThinksThatSchemaIsModified = modified;
				parentTab.setTabContentModified(modified);
			}

			@Override
			public void command_onLoadCallback(int absoluteX, int absoluteY)
			{
				KineticComponent.this.absoluteLeft = absoluteX;
				KineticComponent.this.absoluteTop = absoluteY;
			}

			@Override
			public void response_sendExperimentToSave(ExperimentMetadata metadata, Experiment experiment)
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void response_reloadVisualStyle()
			{
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void setParentTab(CustomTabSheetTabComponent parentTab)
	{
		this.parentTab = parentTab;
	}
	
	public boolean isContentModified()
	{
		return getState().serverThinksThatSchemaIsModified;
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
