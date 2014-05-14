package org.pikater.web.vaadin.gui.client.kineticeditor;

import org.pikater.shared.experiment.webformat.SchemaDataSource;

import com.vaadin.shared.AbstractComponentState;

public class KineticEditorState extends AbstractComponentState
{
	private static final long serialVersionUID = 7400546695911691608L;
	
	/*
	 * GWT SERIALIZATION ISSUES:
	 * - GENERAL - Classes used in GWT RPC have to implement isSerializable or Serializable.
	 * - GENERAL - Default constructor of any visibility makes GWT happy.
	 * - GENERAL - Final fields are not serialized in GWT.
	 * - SPECIFIC - String.format method is not translatable to GWT.
	 * For more information:
	 * http://www.gwtproject.org/doc/latest/DevGuideServerCommunication.html#DevGuideSerializableTypes
	 */
	
	//---------------------------------------------------------------------
	// APPLICATION-WIDE BOX DEFINITIONS
	
	/*
	 * TODO: instead of sharing the box definitions like this, use a UI extension to make it "global"?
	 * Well, it would have to get transferred every time a user opens that UI... unless only called when needed - a boolean value indicating whether it had been sent already.
	 * 
	 * Another possibility would be to wrap this feature in a dedicated component. Doh... terrible but doable.
	 */
	
	/**
	 * The previously defined experiment to view in the editor. It is expected expected to be valid and will be loaded
	 * when the state is shared with the client.
	 * Has to be set on the server-component before being pushed to the client.
	 */
	public SchemaDataSource experimentToLoad = null;
}