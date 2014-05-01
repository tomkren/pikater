package org.pikater.web.vaadin.gui.client.kineticeditor;

import net.edzard.kinetic.Vector2d;

import org.pikater.web.vaadin.gui.client.ClientVars;
import org.pikater.web.vaadin.gui.client.ICustomVaadinWidget;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticShapeCreator.NodeRegisterType;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.operations.TempDeselectOperation;

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

public class KineticEditorWidget extends VerticalPanel implements ICustomVaadinWidget
{
	/**
	 * Reference to the client connector communicating with the server.	
	 */
	private KineticEditorServerRpc server;
	
	/**
	 * Inner GWT variables to keep track of.
	 */
	private final Window.ClosingHandler closingHandler;
	private boolean closingHandlerAdded;
	
	/**
	 * Inner GWT GUI.
	 */
	private FlowPanel toolbar;
	private KineticEditorCanvas kineticCanvas;
	
	/**
	 * Custom widgets to remember and use in event handlers.
	 */
	private PopupPanel jsonComparisonPanel;
	private TextArea leftTextArea;
	private TextArea rightTextArea;
	
	public KineticEditorWidget()
	{
		super();
		
		this.server = null;
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
						closingEvent.setMessage(ClientVars.translation.confirm_KineticLeaveOrLoad());
					}
				}
			}
		};
		this.closingHandlerAdded = false;
		
		// first, setup the toolbar
		setupToolbar();
		
		// then, setup the kinetic canvas
		this.kineticCanvas = new KineticEditorCanvas();
		
		// TODO: delete this eventually:
		BoxPrototype b1 = this.kineticCanvas.getShapeCreator().createBox(NodeRegisterType.MANUAL, "Super box 1", new Vector2d(10, 10), new Vector2d(200, 100));
		BoxPrototype b2 = this.kineticCanvas.getShapeCreator().createBox(NodeRegisterType.MANUAL, "Super box 2", new Vector2d(500, 10), new Vector2d(200, 100));
		BoxPrototype b3 = this.kineticCanvas.getShapeCreator().createBox(NodeRegisterType.MANUAL, "Super box 3", new Vector2d(400, 300), new Vector2d(200, 100));
		EdgePrototype e1 = this.kineticCanvas.getShapeCreator().createEdge(NodeRegisterType.MANUAL, b1, b2);
		this.kineticCanvas.getEngine().registerCreated(b1, b2, b3, e1);
		
		// then, setup the debug panel if necessary
		if(ClientVars.DEBUG_MODE)
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
		if(ClientVars.DEBUG_MODE)
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
	
	@Override
	public void setServerRPC(ServerRpc rpc)
	{
		this.server = (KineticEditorServerRpc) rpc;
	}
	
	public void loadExperiment()
	{
		// TODO:
	}
	
	public void setToolbarVisible(boolean visible)
	{
		this.toolbar.setVisible(visible);
	}
	
	// ----------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void setupToolbar()
	{
		this.toolbar = new FlowPanel();
		if(ClientVars.DEBUG_MODE)
		{
			VButton btn_setLeftTA1 = new VButton();
			btn_setLeftTA1.setText("Set boxes layer (left)");
			btn_setLeftTA1.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					leftTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_BOXES, ClientVars.jsonAttrsToSerialize));
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
					leftTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_EDGES, ClientVars.jsonAttrsToSerialize));
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
					rightTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_SELECTION, ClientVars.jsonAttrsToSerialize));
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
						ClientVars.alertNotImplemented.execute();
						
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
				// TODO:
				ClientVars.alertNotImplemented.execute();
				
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