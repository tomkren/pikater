package org.pikater.web.vaadin.gui.client.kineticcomponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Vector2d;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.SchemaDataSource;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTKeyboardManager;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator.NodeRegisterType;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.KineticUndoRedoManager;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.CreateEdgePlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.DragEdgePlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.SelectionPlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.TrackMousePlugin;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

@SuppressWarnings("serial")
public class KineticComponentWidget extends FocusPanel implements KineticComponentClientRpc
{
	// ------------------------------------------------------
	// GWT RELATED FIELDS

	/**
	 * The panel that will contain the kinetic environment. Needed to increase the size of the kinetic environment, should the need arise.
	 */
	private final ScrollPanel scrollPanel;
	
	// ------------------------------------------------------
	// PROGRAMMATIC FIELDS
	
	/**
	 * Reference to the client connector communicating with the server.	
	 */
	private final KineticComponentConnector connector;
	
	// ------------------------------------------------------
	// EXPERIMENT RELATED FIELDS
	
	/**
	 * All-purpose kinetic engine components.
	 */
	private KineticEngine kineticState;
	private KineticShapeCreator kineticCreator;
	private KineticUndoRedoManager undoRedoManager;
	
	/**
	 * Backup of the last loaded experiment. Since it is shared in the component's state, we have
	 * to know when it is changed and when it is not.	
	 */
	private SchemaDataSource lastLoadedExperiment;
	
	/**
	 * An indicator, whether the component has had kinetic environment created and post-processed correctly. 
	 */
	private boolean isComponentReadyForAnExperiment;
	
	// --------------------------------------------------------
	// CONSTRUCTOR
	
	public KineticComponentWidget(KineticComponentConnector connector)
	{
		super();
		this.connector = connector;
		
		/*
		 * Do the GWT related stuff.
		 */
		
		this.scrollPanel = new ScrollPanel();
		setWidget(this.scrollPanel);
		
		/*
		 * Do the kinetic stuff.
		 */
		
		this.isComponentReadyForAnExperiment = false;
		
		// initialize kinetic managers
		this.kineticState = new KineticEngine(this, Kinetic.createStage(getKineticEnvParentElement()));
		this.kineticCreator = new KineticShapeCreator(this.kineticState);
		this.undoRedoManager = new KineticUndoRedoManager();
		
		// when the GWT event loop finishes and the client size is known
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
		    public void execute()
			{
				/*
				 * TODO: only use this to expand... if one of the new dimensions is smaller than it was, only shrink the parent widget
				 * This is not going to be extremely trivial...
				 */
				
				Element elementWithKnownSize = getElement();
				getEngine().resize(elementWithKnownSize.getOffsetWidth(), elementWithKnownSize.getOffsetHeight());
				
				KineticComponentWidget.this.isComponentReadyForAnExperiment = true;
		    }
		});
		
		// add plugins to the engine
		// IMPORTANT: don't violate the call order - it is very important for correct functionality since plugins may depend upon others
		this.kineticState.addPlugin(new TrackMousePlugin(this.kineticState));
		this.kineticState.addPlugin(new DragEdgePlugin(this.kineticState));
		this.kineticState.addPlugin(new CreateEdgePlugin(kineticState));
		this.kineticState.addPlugin(new SelectionPlugin(kineticState));
		
		/*
		 * Do the rest.
		 */
		
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
	public void createBox(BoxInfo info, int posX, int posY)
	{
		getShapeCreator().createBox(NodeRegisterType.AUTOMATIC, info, new Vector2d(posX, posY));
	}
	
	@Override
	public void loadExperiment(SchemaDataSource experiment)
	{
		if(experiment != null)
		{
			// first reset if necessary
			if(lastLoadedExperiment != null)
			{
				resetKineticEnvironment();
			}
			
			// and then set
			doLoadExperiment(experiment);
		}
	}
	
	@Override
	public void resetKineticEnvironment()
	{
		getEngine().resetEnvironment();
		getUndoRedoManager().clear();
	}
	
	// *****************************************************************************************************
	// OTHER PUBLIC INTERFACE
	
	public Element getKineticEnvParentElement()
	{
		return scrollPanel.getElement();
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
	
	public KineticComponentServerRpc getServerRPC()
	{
		return connector.serverRPC;
	}
	
	public KineticComponentState getSharedState()
	{
		return connector.getState();
	}
	
	// *****************************************************************************************************
	// PRIVATE INTERFACE
	
	private void doLoadExperiment(final SchemaDataSource newExperiment)
	{
		// create a new timer that checks whether the component has been attached and ready periodically
        Timer t = new Timer()
        {
        	@Override
        	public void run()
        	{
        		if(isComponentReadyForAnExperiment)
        		{
    				KineticShapeCreator shapeCreator = getShapeCreator();
    				
    				// first convert all boxes
    				Map<Integer, BoxPrototype> guiBoxes = new HashMap<Integer, BoxPrototype>();
    				for(Integer leafBoxID : newExperiment.leafBoxes.keySet())
    				{
    					BoxPrototype guiBox = shapeCreator.createBox(NodeRegisterType.MANUAL, newExperiment.leafBoxes.get(leafBoxID));
    					guiBoxes.put(leafBoxID, guiBox);
    				}
    				
    				// then convert all edges
    				Collection<ExperimentGraphItem> allGraphItems = new ArrayList<ExperimentGraphItem>(guiBoxes.values()); // boxes should to be registered before edges
    				for(Entry<Integer, Set<Integer>> entry : newExperiment.edges.entrySet())
    				{
    					for(Integer toLeafBoxID : entry.getValue())
    					{
    						BoxPrototype fromBox = guiBoxes.get(entry.getKey());
    						BoxPrototype toBox = guiBoxes.get(toLeafBoxID);
    						allGraphItems.add(shapeCreator.createEdge(NodeRegisterType.MANUAL, fromBox, toBox));
    					}
    				}
    				
    				// put everything into Kinetic
    				getEngine().registerCreated(allGraphItems.toArray(new ExperimentGraphItem[0]));
    				
    				// and remember the loaded instance
    				lastLoadedExperiment = newExperiment;
        			
	        		KineticComponentWidget.this.scrollPanel.scrollToLeft();
	        		KineticComponentWidget.this.scrollPanel.scrollToTop();
	        		
	        		cancel();
        		}
        	}
        };

        // schedule the timer to run once in a second
        t.schedule(1000);
	}
}