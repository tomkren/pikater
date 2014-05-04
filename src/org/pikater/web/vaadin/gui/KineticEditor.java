package org.pikater.web.vaadin.gui;

import org.pikater.shared.experiment.ConversionException;
import org.pikater.shared.experiment.UniToWebConverter;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.webformat.SchemaDataSource;
import org.pikater.web.WebAppLogger;
import org.pikater.web.vaadin.gui.client.kineticeditor.KineticEditorServerRpc;
import org.pikater.web.vaadin.gui.client.kineticeditor.KineticEditorState;

import com.vaadin.ui.AbstractComponent;

public class KineticEditor extends AbstractComponent
{
	private static final long serialVersionUID = -539901377528727478L;
	
	private final KineticEditorServerRpc rpc = new KineticEditorServerRpc()
	{
		private static final long serialVersionUID = -2769231541745495584L;
	};

	public KineticEditor()
	{
		registerRpc(rpc);
		
		// to call client side RPC:
		// getRpcProxy(KineticEditorClientRpc.class).experimentChangedCallback();
	}
	
	public void loadExperiment(UniversalComputationDescription experiment)
	{
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
	}

	@Override
	public KineticEditorState getState()
	{
		return (KineticEditorState) super.getState();
	}
}
