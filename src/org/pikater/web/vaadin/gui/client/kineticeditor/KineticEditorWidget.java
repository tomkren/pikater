package org.pikater.web.vaadin.gui.client.kineticeditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pikater.shared.experiment.webformat.SchemaDataSource;
import org.pikater.web.vaadin.gui.client.jsni.JSNI_SharedConfig;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator.NodeRegisterType;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.TempDeselectOperation;
import org.pikater.web.vaadin.gui.client.managers.GWTMisc;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vaadin.client.ui.VButton;
import com.vaadin.shared.communication.ServerRpc;

public class KineticEditorWidget extends VerticalPanel
{
	// --------------------------------------------------------
	// GWT GUI
	
	/**
	 * Main widgets.
	 */
	private FlowPanel toolbar;
	private final KineticEditorCanvas kineticCanvas;
	
	/**
	 * Special debug components.
	 */
	private PopupPanel jsonComparisonPanel;
	private TextArea leftTextArea;
	private TextArea rightTextArea;
	
	// --------------------------------------------------------
	// VARIOUS VARIABLES
	
	/**
	 * Reference to the client connector communicating with the server.	
	 */
	private KineticEditorServerRpc server;
	
	/**
	 * Backup of the last loaded experiment. Since it is shared in the component's state, we have
	 * to know when it is changed and when it is not.	
	 */
	private SchemaDataSource lastLoadedExperiment;
	
	/**
	 * Inner GWT variables to keep track of.
	 */
	private final Window.ClosingHandler closingHandler;
	private boolean closingHandlerAdded;
	
	// --------------------------------------------------------
	// CONSTRUCTOR
	
	public KineticEditorWidget()
	{
		super();
		
		this.server = null;
		this.lastLoadedExperiment = null;
		this.closingHandler = new Window.ClosingHandler()
		{
			/*
			 * Code to prevent users from accidentally navigating away while having unsaved changes in the
			 * kinetic canvas. This handler is added only once on the first widget load (see below).
			 * (non-Javadoc). @see com.google.gwt.user.client.Window.ClosingHandler#onWindowClosing(com.google.gwt.user.client.Window.ClosingEvent)
			 */
			
			@Override
			public void onWindowClosing(Window.ClosingEvent closingEvent)
			{
				if(isAttached())
				{
					if(kineticCanvas.existsUnsavedContent())
					{
						closingEvent.setMessage("Are you sure? Editor content will be lost, if unsaved.");
					}
				}
			}
		};
		this.closingHandlerAdded = false;
		
		// first, setup the toolbar
		setupToolbar();
		this.toolbar.addStyleName("showBorder"); // showBorder
		
		// then, setup the kinetic canvas
		this.kineticCanvas = new KineticEditorCanvas();
		this.kineticCanvas.addStyleName("showBorder");
		
		// then, setup the debug panel if necessary
		if(JSNI_SharedConfig.isDebugModeActivated())
		{
			setupDebugPanel();
		}
		
		// and finally, add the components to this widget
		this.setSpacing(10);
		this.add(toolbar);
		this.add(kineticCanvas);
	}
	
	@Override
	protected void onLoad()
	{
		super.onLoad();
		if(!closingHandlerAdded)
		{
			Window.addWindowClosingHandler(closingHandler);
			closingHandlerAdded = true;
		}
		if(JSNI_SharedConfig.isDebugModeActivated())
		{
			String mainWidth = String.valueOf(this.getOffsetWidth()) + "px";
			String halfWidth = String.valueOf(this.getOffsetWidth() / 2) + "px";
			
			jsonComparisonPanel.setWidth(mainWidth);
			leftTextArea.setWidth(halfWidth);
			rightTextArea.setWidth(halfWidth);
			
			String height = "800px";
			jsonComparisonPanel.setHeight(height);
		}
	}
	
	public KineticEditorServerRpc getServerRPC()
	{
		return server;
	}
	
	public void setToolbarVisible(boolean visible)
	{
		this.toolbar.setVisible(visible);
	}
	
	// ----------------------------------------------------------------------
	// METHODS TO CALL FROM THE CONNECTOR
	
	public void setServerRPC(ServerRpc rpc)
	{
		this.server = (KineticEditorServerRpc) rpc;
	}
	
	public void loadExperiment(SchemaDataSource newExperiment)
	{
		if(lastLoadedExperiment != newExperiment)
		{
			if(newExperiment != null)
			{
				KineticShapeCreator shapeCreator = this.kineticCanvas.getShapeCreator();
				
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
				this.kineticCanvas.getEngine().registerCreated(allGraphItems.toArray(new ExperimentGraphItem[0]));
				
				// and remember the loaded instance
				lastLoadedExperiment = newExperiment;
			}
			else
			{
				// TODO: clear everything
			}
		}
		// else - do nothing
	}
	
	// ----------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void setupToolbar()
	{
		this.toolbar = new FlowPanel();
		if(JSNI_SharedConfig.isDebugModeActivated())
		{
			VButton btn_setLeftTA1 = new VButton();
			btn_setLeftTA1.setText("Set boxes layer (left)");
			btn_setLeftTA1.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					leftTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_BOXES, GWTMisc.jsonAttrsToSerialize));
				}
			});
			// btn_setLeftTA1.addStyleName("pointCursorOnHover");
			toolbar.add(btn_setLeftTA1);
			
			VButton btn_setLeftTA2 = new VButton();
			btn_setLeftTA2.setText("Set edge layer (left)");
			btn_setLeftTA2.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					leftTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_EDGES, GWTMisc.jsonAttrsToSerialize));
					leftTextArea.setText(kineticCanvas.getEngine().serializeToJSON(EngineComponent.LAYER_EDGES));
				}
			});
			// btn_setLeftTA2.setStyleName("pointCursorOnHover");
			toolbar.add(btn_setLeftTA2);
			
			VButton btn_setRightTA = new VButton();
			btn_setRightTA.setText("Set selection (right)");
			btn_setRightTA.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					rightTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_SELECTION, GWTMisc.jsonAttrsToSerialize));
				}
			});
			// btn_setRightTA.setStyleName("pointCursorOnHover");
			toolbar.add(btn_setRightTA);
			
			VButton btn_displayComparison = new VButton();
			btn_displayComparison.setText("Display JSON comparison");
			btn_displayComparison.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					jsonComparisonPanel.show();
				}
			});
			// btn_displayComparison.setStyleName("pointCursorOnHover");
			toolbar.add(btn_displayComparison);
		}
		
		VButton btn_saveStageJSON = new VButton();
		btn_saveStageJSON.setText("Serialize");
		btn_saveStageJSON.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				new TempDeselectOperation(kineticCanvas.getEngine(), new Command()
				{
					@Override
					public void execute()
					{
						// TODO:
						GWTMisc.alertNotImplemented.execute();
						
						/*
						server.experimentSchemaSerialized(
								kineticCanvas.getEngine().serializeToJSON(EngineComponent.LAYER_BOXES),
								kineticCanvas.getEngine().getEdgeListJSON()
						);
						*/
					}
				});
			}
		});
		// btn_saveStageJSON.setStyleName("pointCursorOnHover");
		toolbar.add(btn_saveStageJSON);
		
		VButton btn_loadStageJSON = new VButton();
		btn_loadStageJSON.setText("Deserialize");
		btn_loadStageJSON.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GWTMisc.alertNotImplemented.execute();
				
				// kineticState.deserialize(dLayerJSON, edgeListJSON);
				// setFocus(true);
			}
		});
		// btn_loadStageJSON.setStyleName("pointCursorOnHover");
		toolbar.add(btn_loadStageJSON);
	}
	
	private void setupDebugPanel()
	{
		leftTextArea = new TextArea();
		leftTextArea.setHeight("100%");
		rightTextArea = new TextArea();
		rightTextArea.setHeight("100%");
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		hPanel.setHeight("100%");
		hPanel.add(leftTextArea);
		hPanel.add(rightTextArea);
		
		jsonComparisonPanel = new PopupPanel(true);
		jsonComparisonPanel.add(hPanel);
	}
}