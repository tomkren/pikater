package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.ExperimentGraph;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTKeyboardManager;
import org.pikater.web.vaadin.gui.client.kineticengine.IKineticEngineContext;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator.NodeRegisterType;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.KineticUndoRedoManager;
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

public class KineticComponentWidget extends FocusPanel implements KineticComponentClientRpc, KineticComponentServerRpc, IKineticEngineContext
{
	private static final long serialVersionUID = 946534795907059986L;
	
	// ------------------------------------------------------
	// PROGRAMMATIC FIELDS
	
	/**
	 * Reference to the client connector communicating with the server.	
	 */
	private final KineticComponentConnector connector;
	
	// ------------------------------------------------------
	// EXPERIMENT RELATED FIELDS
		
	private KineticState state;
	
	// --------------------------------------------------------
	// CONSTRUCTOR
	
	public KineticComponentWidget(KineticComponentConnector connector)
	{
		super();
		
		/*
		 * Do the rest.
		 */
		
		this.connector = connector;
		this.state = null;
		
		// handlers to register keys being pushed down and released when the editor has focus
		addKeyDownHandler(new KeyDownHandler()
		{
			@Override
			public void onKeyDown(KeyDownEvent event)
			{
				switch (event.getNativeKeyCode())
				{
					case KeyCodes.KEY_BACKSPACE:
						getEngine().deleteSelected();
						break;
					case 90: // Z
						if(GWTKeyboardManager.isControlKeyDown())
						{
							getHistoryManager().undo();
						}
						break;
					case 89: // Y
						if(GWTKeyboardManager.isControlKeyDown())
						{
							getHistoryManager().redo();
						}
						break;
					case 87: // W
						if(GWTKeyboardManager.isAltKeyDown())
						{
							// the click mode will really be changed on the server...
							command_alterClickMode(getClickMode().getOther());
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
	
	public KineticState getState()
	{
		return state;
	}
	
	public void initState(KineticState backup)
	{
		if(backup != null)
		{
			this.state = backup;
			this.state.setParentWidget(this);
		}
		else
		{
			this.state = new KineticState(this);
		}
		
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
				
				// and finally, draw the stage
				getEngine().draw(EngineComponent.STAGE);
			}
		});
	}
	
	// *****************************************************************************************************
	// COMMANDS FROM SERVER
	
	@Override
	public void command_createBox(final BoxInfo info)
	{
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
		    public void execute()
			{
				getShapeCreator().createBox(NodeRegisterType.AUTOMATIC, info);
		    }
		});
	}
	
	/**
	 * Loads the provided experiment into the kinetic environment encapsulated by this widget. If null,
	 * resets the environment.
	 */
	@Override
	public void command_receiveExperimentToLoad(final ExperimentGraph experiment)
	{
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
		    public void execute()
			{
				if(experiment != null)
				{
					// first reset
					command_resetKineticEnvironment();
					
					// and then load the experiment
					getEngine().fromIntermediateFormat(experiment);
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
				getEngine().destroyGraphAndClearStage();
				getHistoryManager().clear();
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
	public void request_sendExperimentToSave()
	{
		response_sendExperimentToSave(getEngine().toIntermediateFormat());
	}
	
	// *****************************************************************************************************
	// COMMANDS TO SERVER - SIMPLE FORWARDING
	
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
	public void command_alterClickMode(KineticComponentClickMode newClickMode)
	{
		getServerRPC().command_alterClickMode(newClickMode);
	}
	
	@Override
	public void command_openOptionsManager(String[] selectedBoxesAgentIDs)
	{
		getServerRPC().command_openOptionsManager(selectedBoxesAgentIDs);
	}
	
	@Override
	public void response_reloadVisualStyle()
	{
		getServerRPC().response_reloadVisualStyle();
	}
	
	@Override
	public void response_sendExperimentToSave(ExperimentGraph experiment)
	{
		getServerRPC().response_sendExperimentToSave(experiment);
		
		// TODO: only do this if saving the experiment is successful?
		getHistoryManager().clear();
	}
	
	// *****************************************************************************************************
	// KINETIC CONTEXT INTERFACE
	
	@Override
	public Element getStageDOMElement()
	{
		return getElement();
	}
	
	@Override
	public KineticEngine getEngine()
	{
		return state.getEngine();
	}
	
	@Override
	public KineticUndoRedoManager getHistoryManager()
	{
		return state.getHistoryManager();
	}

	@Override
	public KineticShapeCreator getShapeCreator()
	{
		return state.getShapeCreator();
	}
	
	@Override
	public KineticComponentClickMode getClickMode()
	{
		return connector.getState().clickMode;
	}
	
	@Override
	public boolean openOptionsManagerOnSelectionChange()
	{
		return connector.getState().openOptionsOnSelection;
	}
	
	// *****************************************************************************************************
	// PRIVATE INTERFACE
	
	private KineticComponentServerRpc getServerRPC()
	{
		return connector.serverRPC;
	}
}