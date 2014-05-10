package org.pikater.web.vaadin.gui.client.kineticeditor;

import net.edzard.kinetic.Kinetic;

import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticShapeCreator;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.operations.undoredo.KineticUndoRedoManager;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins.CreateEdgePlugin;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins.DragEdgePlugin;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins.SelectionPlugin;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins.TrackMousePlugin;
import org.pikater.web.vaadin.gui.client.managers.GWTKeyboardManager;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * The widget encapsulating the experiment schema editor. It extends FocusPanel because its functionalities are needed for mouse event handling.
 */
public class KineticEditorCanvas extends FocusPanel
{
	/**
	 * The panel that will contain the kinetic environment. Needed to increase the size of the kinetic environment, should the need arise.
	 */
	private final ScrollPanel scrollPanel;
	
	/**
	 * All-purpose kinetic engine components.
	 */
	private final KineticEngine kineticState;
	private final KineticShapeCreator kineticCreator;
	private final KineticUndoRedoManager undoRedoManager;

	public KineticEditorCanvas()
	{
		super();
		
		this.scrollPanel = new ScrollPanel();
		// this.scrollPanel.setSize(String.valueOf((int)this.minimumKineticStageSize.x) + "px", String.valueOf((int)this.minimumKineticStageSize.y) + "px");
		this.scrollPanel.setSize("800px", "600px");
		this.scrollPanel.scrollToLeft();
		this.scrollPanel.scrollToTop();
		setWidget(this.scrollPanel);
		
		/*
		// set action modifier key
		// resource for this: http://stackoverflow.com/questions/3902635/how-does-one-capture-a-macs-command-key-via-javascript
		switch (JavascriptEntryPoint.getUnderlyingOS())
		{
			case MAC_OS: // meta key
				switch ()
				this.actionModifierKey = 224;
				break;
			default: // control key
				this.actionModifierKey = KeyCodes.KEY_CTRL;
				break;
		}
		*/
		
		// initialize kinetic managers
		this.undoRedoManager = new KineticUndoRedoManager();
		// this.kineticState = new KineticEngine(this, Kinetic.createStage(getKineticEnvironmentDOMElementParent(), (int)this.minimumKineticStageSize.x, (int)this.minimumKineticStageSize.y));
		this.kineticState = new KineticEngine(this, Kinetic.createStage(getDOMElement(), 800, 600));
		this.kineticCreator = new KineticShapeCreator(this.kineticState);
		
		// add plugins to the engine
		// IMPORTANT: don't violate the call order - it is very important for correct functionality since plugins may depend upon others
		this.kineticState.addPlugin(new TrackMousePlugin(this.kineticState));
		this.kineticState.addPlugin(new DragEdgePlugin(this.kineticState));
		this.kineticState.addPlugin(new CreateEdgePlugin(kineticState));
		this.kineticState.addPlugin(new SelectionPlugin(kineticState));
		
		// handlers to register keys being pushed down and released when the editor has focus
		addKeyDownHandler(new KeyDownHandler()
		{
			@Override
			public void onKeyDown(KeyDownEvent event)
			{
				switch (event.getNativeKeyCode())
				{
					case KeyCodes.KEY_BACKSPACE:
						kineticState.deleteSelected();
						break;
					case 90: // Z
						if(GWTKeyboardManager.isControlKeyDown())
						{
							undoRedoManager.undo();
						}
						break;
					case 89: // Y
						if(GWTKeyboardManager.isControlKeyDown())
						{
							undoRedoManager.redo();
						}
						break;
					default:
						System.out.println("KeyCode down: " + event.getNativeEvent().getKeyCode());
						break;
				}
				event.stopPropagation();
			}
		});
		addMouseOverHandler(new MouseOverHandler()
		{
			@Override
			public void onMouseOver(MouseOverEvent event)
			{
				setFocus(true); // there is no cross-browser support for "isFocused" method so just set focus anyway :)
			}
		});
	}
	
	// *****************************************************************************************************
	// PUBLIC INTERFACE
	
	public Element getDOMElement()
	{
		return scrollPanel.getElement();
	}
	
	public KineticUndoRedoManager getUndoRedoManager()
	{
		return undoRedoManager;
	}
	
	public KineticShapeCreator getShapeCreator()
	{
		return kineticCreator;
	}
	
	public boolean existsUnsavedContent()
	{
		return undoRedoManager.existsUnsavedContent();
	}
	
	public KineticEngine getEngine()
	{
		return kineticState;
	}
}