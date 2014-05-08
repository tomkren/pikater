package org.pikater.web.vaadin.gui;

import org.pikater.shared.experiment.webformat.BoxInfoCollection;
import org.pikater.shared.experiment.webformat.SchemaDataSource;
import org.pikater.web.vaadin.gui.client.kineticeditor.KineticEditorServerRpc;
import org.pikater.web.vaadin.gui.client.kineticeditor.KineticEditorState;

import com.vaadin.ui.AbstractComponent;

public class KineticEditor extends AbstractComponent
{
	private static final long serialVersionUID = -539901377528727478L;
	
	public KineticEditor()
	{
		// first define server RPC
		registerRpc(new KineticEditorServerRpc()
		{
			private static final long serialVersionUID = -2769231541745495584L;
		});
		
		// getRpcProxy(KineticEditorClientRpc.class).response_BoxInfoProvider(SchemaDataSource.infoProvider);
	}
	
	public void setBoxDefinitions(BoxInfoCollection boxDefinitions)
	{
		getState().infoProvider = boxDefinitions;
	}
	
	public void setExperimentToLoad(SchemaDataSource experiment)
	{
		getState().experimentToLoad = experiment;
		
		/*
		try
		{
			SchemaDataSource webFormat = UniToWebConverter.convert(experiment, KineticEditorState.getBoxInfoProvider());
			getState().setExperimentToLoad(webFormat);
		}
		catch (ConversionException e)
		{
			// TODO: convert to XML and append to the log
			WebAppLogger.logThrowable(
					String.format("An error occured when trying to convert the following %s to the web format:\n", UniversalComputationDescription.class.getSimpleName()),
					e
			);
		}
		*/
	}

	@Override
	public KineticEditorState getState()
	{
		return (KineticEditorState) super.getState();
	}
}
