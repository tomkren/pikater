package org.pikater.web.vaadin.gui.client.kineticeditor;

import org.pikater.shared.experiment.webformat.WebBoxInfoProvider;
import org.pikater.shared.experiment.webformat.SchemaDataSource;

import com.vaadin.shared.AbstractComponentState;

public class KineticEditorState extends AbstractComponentState
{
	private static final long serialVersionUID = 7400546695911691608L;
	
	//---------------------------------------------------------------------
	// APPLICATION-WIDE BOX DEFINITIONS
	
	private static WebBoxInfoProvider infoProvider;
	
	public static WebBoxInfoProvider getBoxInfoProvider()
	{
		return infoProvider;
	}

	public static void setBoxInfo(WebBoxInfoProvider infoProvider)
	{
		KineticEditorState.infoProvider = infoProvider;
	}
	
	public static boolean isInfoProviderSet()
	{
		return getBoxInfoProvider() != null;
	}

	//---------------------------------------------------------------------
	// SHARING INITIAL EXPERIMENTS TO LOAD
	
	private SchemaDataSource experimentToLoad = null;
	
	public SchemaDataSource getExperimentToLoad()
	{
		return experimentToLoad;
	}

	/**
	 * The argument experiment is expected to be valid and will be loaded when the state is shared with the client.
	 * @param experimentToLoad
	 */
	public void setExperimentToLoad(SchemaDataSource experimentToLoad)
	{
		if(experimentToLoad == null)
		{
			throw new NullPointerException();
		}
		else if(!isInfoProviderSet())
		{
			throw new IllegalStateException("Setting an experiment requires a previously set box info provider.");
		}
		else
		{
			this.experimentToLoad = experimentToLoad;
		}
	}
}