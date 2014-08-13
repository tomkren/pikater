package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.shared.experiment.webformat.client.BoxInfoClient;
import org.pikater.shared.experiment.webformat.client.ExperimentGraphClient;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTKeyboardManager;
import org.pikater.web.vaadin.gui.client.kineticengine.IKineticEngineContext;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.GraphItemCreator;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticUndoRedoManager;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.DeleteSelectedBoxesOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.GraphItemCreator.GraphItemRegistration;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.ClickMode;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.AbstractGraphItemShared.RegistrationOperation;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.BoxGraphItemShared;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.EdgeGraphItemShared;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
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
						getEngine().pushNewOperation(new DeleteSelectedBoxesOperation(getEngine()));
						event.preventDefault();
						event.stopPropagation();
						break;
					case 90: // Z
						if(GWTKeyboardManager.isControlKeyDown())
						{
							getHistoryManager().undo();
							event.preventDefault();
							event.stopPropagation();
						}
						break;
					case 89: // Y
						if(GWTKeyboardManager.isControlKeyDown())
						{
							getHistoryManager().redo();
							event.preventDefault();
							event.stopPropagation();
						}
						break;
					case 87: // W
						if(GWTKeyboardManager.isAltKeyDown())
						{
							// the click mode will really be changed on the server...
							command_alterClickMode(getClickMode().getOther());
							event.preventDefault();
							event.stopPropagation();
						}
						break;
					default:
						// GWTLogger.logWarning("KeyCode down: " + event.getNativeEvent().getKeyCode());
						break;
				}
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
		
		Window.addResizeHandler(new ResizeHandler()
		{
			@Override
			public void onResize(ResizeEvent event)
			{
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand()
				{
					@Override
					public void execute()
					{
						Window.alert("RESIZE DETECTED");
					}
				});
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
				command_onLoadCallback(getAbsoluteLeft(), getAbsoluteTop());
				
				// and finally, draw the stage
				getEngine().draw(EngineComponent.STAGE);
			}
		});
	}
	
	// *****************************************************************************************************
	// COMMANDS FROM SERVER
	
	/*
	 * IMPORTANT: don't call other commands from inside commands. Because we use
	 * scheduler, the action are simply enqueued and hence any action called
	 * will actually be performed after the current one finishes. 
	 */
	
	@Override
	public void highlightBoxes(final Integer[] boxIDs)
	{
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
			public void execute()
			{
				getEngine().highlightUntilNextRepaint(boxIDs);
			}
		});
	}

	@Override
	public void cancelBoxHighlight()
	{
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
			public void execute()
			{
				getEngine().draw(EngineComponent.LAYER_BOXES);
			}
		});
	}
	
	@Override
	public void cancelSelection()
	{
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
			public void execute()
			{
				getEngine().cancelSelection();
			}
		});
	}

	@Override
	public void resetEnvironment()
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
	
	/**
	 * Loads the provided experiment into the kinetic environment encapsulated by this widget. If null,
	 * resets the environment.
	 */
	@Override
	public void receiveExperimentToLoad(final ExperimentGraphClient experiment)
	{
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
			public void execute()
			{
				if(experiment != null)
				{
					getEngine().fromIntermediateFormat(experiment);
				}
				else
				{
					resetEnvironment(); // in this case we don't mind calling other command
				}
			}
		});
	}
	
	@Override
	public void createBox(final BoxInfoClient info)
	{	
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
			public void execute()
			{
				getGraphItemCreator().createBox(GraphItemRegistration.AUTOMATIC, info);
			}
		});
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
	public void command_alterClickMode(ClickMode newClickMode)
	{
		getServerRPC().command_alterClickMode(newClickMode);
	}
	
	@Override
	public void command_selectionChange(Integer[] selectedBoxesAgentIDs)
	{
		getServerRPC().command_selectionChange(selectedBoxesAgentIDs);
	}
	
	/*
	 * VAADIN'S BUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUG!
	 * Vaadin's RPC always takes the latest method with the same name and erasure (ignoring types), e.g.:
	 * 
	 * class Whatever1 implements Serializable
	 * {
	 * }
	 * 
	 * class Whatever2 implements Serializable
	 * {
	 * }
	 * 
	 * void foo(Whatever1 whatever1);
	 * void foo(Whatever2 whatever2);
	 * 
	 * When calling "rpc.foo(Whatever1)", "foo(Whatever2)" is used anyway.
	 * 
	@Override
	public void command_itemSetChange(RegistrationOperation opKind, EdgeGraphItemShared[] edges)
	{
		getServerRPC().command_itemSetChange(opKind, edges);
	}
	*/
	
	@Override
	public void command_boxSetChange(RegistrationOperation opKind, BoxGraphItemShared[] boxes)
	{
		getServerRPC().command_boxSetChange(opKind, boxes);
	}

	@Override
	public void command_edgeSetChange(RegistrationOperation opKind, EdgeGraphItemShared[] edges)
	{
		getServerRPC().command_edgeSetChange(opKind, edges);
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
	public GraphItemCreator getGraphItemCreator()
	{
		return state.getGraphItemCreator();
	}
	
	@Override
	public ClickMode getClickMode()
	{
		return connector.getState().clickMode;
	}
	
	// *****************************************************************************************************
	// PRIVATE INTERFACE
	
	private KineticComponentServerRpc getServerRPC()
	{
		return connector.serverRPC;
	}
}