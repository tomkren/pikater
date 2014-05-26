package org.pikater.web.vaadin.gui.client.kineticcomponent;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Vector2d;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.Experiment;
import org.pikater.shared.experiment.webformat.ExperimentMetadata;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTKeyboardManager;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator.NodeRegisterType;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.KineticUndoRedoManager;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.CreateEdgePlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.DragEdgePlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.SelectionPlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.TrackMousePlugin;
import org.pikater.web.vaadin.gui.shared.KineticComponentClickMode;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FocusPanel;

public class KineticComponentWidget extends FocusPanel implements KineticComponentClientRpc, KineticComponentServerRpc
{
	private static final long serialVersionUID = 946534795907059986L;
	
	// ------------------------------------------------------
	// EXPERIMENT RELATED FIELDS
	
	/**
	 * All-purpose kinetic engine components.
	 */
	private final KineticEngine kineticState;
	private final KineticShapeCreator kineticCreator;
	private final KineticUndoRedoManager undoRedoManager;
	
	/**
	 * Backup of the last loaded experiment. Since it is shared in the component's state, we have
	 * to know when it is changed and when it is not.	
	 */
	private Experiment lastLoadedExperiment;
	
	// ------------------------------------------------------
	// PROGRAMMATIC FIELDS
	
	/**
	 * Reference to the client connector communicating with the server.	
	 */
	private final KineticComponentConnector connector;
	
	// --------------------------------------------------------
	// CONSTRUCTOR
	
	public KineticComponentWidget(KineticComponentConnector connector)
	{
		super();
		
		/*
		 * Do the kinetic stuff.
		 */
		
		// initialize kinetic managers
		this.kineticState = new KineticEngine(this, Kinetic.createStage(getKineticEnvParentElement()));
		this.kineticCreator = new KineticShapeCreator(this.kineticState);
		this.undoRedoManager = new KineticUndoRedoManager(this);
		
		// add plugins to the engine
		// IMPORTANT: don't violate the call order - it is very important for correct functionality since plugins may depend upon others
		this.kineticState.addPlugin(new TrackMousePlugin(this.kineticState));
		this.kineticState.addPlugin(new DragEdgePlugin(this.kineticState));
		this.kineticState.addPlugin(new CreateEdgePlugin(kineticState));
		this.kineticState.addPlugin(new SelectionPlugin(kineticState));
		
		// when the GWT event loop finishes and the component is fully read and its information published
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
		    public void execute()
			{
				/*
				 * TODO: only use this to expand... if one of the new dimensions is smaller than it was, only shrink the parent widget
				 * This is not going to be extremely trivial...
				 */
				
				// resize the kinetic stage to fully fill the parent component
				Element elementWithKnownSize = getElement();
				getEngine().resize(elementWithKnownSize.getOffsetWidth(), elementWithKnownSize.getOffsetHeight());
				
				// send information about absolute position to the server so that it can compute relative mouse position
				getServerRPC().command_onLoadCallback(getAbsoluteLeft(), getAbsoluteTop());
		    }
		});
		
		/*
		 * Do the rest.
		 */
		
		this.connector = connector;
		this.lastLoadedExperiment = null;
		
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
					case 87: // W
						if(GWTKeyboardManager.isAltKeyDown())
						{
							// first alter the click mode
							KineticComponentClickMode newClickMode = getSharedState().clickMode.getOther();
							getSharedState().clickMode = newClickMode;
							// and send notification to the server
							command_alterClickModeNotification(newClickMode);
						}
						break;
					default:
						// GWTLogger.logWarning("KeyCode down: " + event.getNativeEvent().getKeyCode());
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
	}
	
	// *****************************************************************************************************
	// COMMANDS FROM SERVER
	
	@Override
	public void command_createBox(final BoxInfo info, final int posX, final int posY)
	{
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
		    public void execute()
			{
				getShapeCreator().createBox(NodeRegisterType.AUTOMATIC, info, new Vector2d(posX, posY));
		    }
		});
	}
	
	/**
	 * Loads the provided experiment into the kinetic environment encapsulated by this widget. If null,
	 * resets the environment.
	 */
	@Override
	public void command_receiveExperimentToLoad(final Experiment experiment)
	{
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
		    public void execute()
			{
				if(experiment != null)
				{
					// first reset if necessary
					if(lastLoadedExperiment != null)
					{
						command_resetKineticEnvironment();
					}
					
					// and then load the experiment
					getEngine().fromIntermediateFormat(experiment);
    				lastLoadedExperiment = experiment;
				}
				else
				{
					command_resetKineticEnvironment();
				}
		    }
		});
	}
	
	@Override
	public void command_resetKineticEnvironment()
	{
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
		    public void execute()
			{
				getEngine().resetEnvironment();
				getUndoRedoManager().clear();
		    }
		});
	}
	
	@Override
	public void request_reloadVisualStyle()
	{
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
		    public void execute()
			{
				getEngine().reloadVisualStyle();
		    }
		});
		
	}
	
	@Override
	public void request_sendExperimentToSave(ExperimentMetadata metadata)
	{
		response_sendExperimentToSave(metadata, getEngine().toIntermediateFormat());
	}
	
	// *****************************************************************************************************
	// COMMANDS TO SERVER
	
	@Override
	public void command_setExperimentModified(boolean modified)
	{
		getServerRPC().command_setExperimentModified(modified);
	}

	@Override
	public void command_onLoadCallback(int absoluteX, int absoluteY)
	{
		getServerRPC().command_onLoadCallback(absoluteX, absoluteY);
	}
	
	@Override
	public void response_reloadVisualStyle()
	{
		getServerRPC().response_reloadVisualStyle();
	}
	
	@Override
	public void response_sendExperimentToSave(ExperimentMetadata metadata, Experiment experiment)
	{
		getServerRPC().response_sendExperimentToSave(metadata, experiment);
		getUndoRedoManager().clear();
	}
	
	@Override
	public void command_alterClickModeNotification(KineticComponentClickMode newClickMode)
	{
		getServerRPC().command_alterClickModeNotification(newClickMode);
	}
	
	// *****************************************************************************************************
	// OTHER PUBLIC INTERFACE
	
	public Element getKineticEnvParentElement()
	{
		return getElement();
	}
	
	public KineticEngine getEngine()
	{
		return kineticState;
	}
	
	public KineticShapeCreator getShapeCreator()
	{
		return kineticCreator;
	}
	
	public KineticUndoRedoManager getUndoRedoManager()
	{
		return undoRedoManager;
	}
	
	public KineticComponentState getSharedState()
	{
		return connector.getState();
	}
	
	// *****************************************************************************************************
	// PRIVATE INTERFACE
	
	private KineticComponentServerRpc getServerRPC()
	{
		return connector.serverRPC;
	}
}