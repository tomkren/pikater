package org.pikater.web.vaadin.gui;

import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
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
	}
	
	public void loadExperiment(UniversalComputationDescription experiment)
	{
		// TODO:
	}

	@Override
	public KineticEditorState getState()
	{
		return (KineticEditorState) super.getState();
	}
}
