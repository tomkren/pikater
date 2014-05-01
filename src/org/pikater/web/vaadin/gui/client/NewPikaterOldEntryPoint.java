package org.pikater.web.vaadin.gui.client;

import net.edzard.kinetic.Vector2d;

import org.pikater.web.vaadin.gui.client.kineticeditor.KineticEditorCanvas;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticShapeCreator.NodeRegisterType;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.operations.TempDeselectOperation;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NewPikaterOldEntryPoint implements EntryPoint
{
	/**
	 * Custom widgets to remember and use in event handlers.
	 */
	private PopupPanel jsonComparisonPanel;
	private TextArea leftTextArea;
	private TextArea rightTextArea;
	
	/**
	 * Entry point into the Kinetic environment.
	 */
	private KineticEditorCanvas kineticCanvas;
	
	/**
	 * Window width and height to compute the size of the widgets.
	 */
	private int clientWidth;
	private int clientHeight;
	
	public void onModuleLoad()
	{
		// IMPORTANT: don't violate the call order - left menu needs to be setup before the kinetic canvas, otherwise an exception will occur.
		setupBannerAndAccountArea();
		setupLeftMenu();
		setupMainPageFrame();
		setupJSONComparisonPanel();
	}
	
	// ***************************************************************************************************************************
	// main methods creating UI
	
	private void setupBannerAndAccountArea()
	{
		// add banner and account interface
		SimplePanel userControlPanel = new SimplePanel();
		userControlPanel.addStyleName("horizontallyCenterAlignedElement");
		userControlPanel.addStyleName("verticallyCenterAlignedElement");
		userControlPanel.add(new Label(ClientVars.translation.userControlPanelLabel()));
		RootPanel.get("td_usercp").add(userControlPanel);
		
		// set the client window size
		clientWidth = userControlPanel.getOffsetWidth() - 30;
		clientHeight = userControlPanel.getOffsetHeight();
	}
	
	private void setupLeftMenu()
	{
		// first create the wrapper widget and insert it into DOM to get its childrens' widths later
		MenuBar menu = new MenuBar(true);
		menu.getElement().setId("mainMenuBar");
		menu.addStyleName("maxHeight");
		menu.addStyleName("showBorder");
		RootPanel.get("td_leftMenu").add(menu);
		
		// add individual menus and compute the minimum needed width
		int maxWidth = 0;
		maxWidth = addMenuItemAndReturnGreatestWidthSoFar(menu, createMenuItem(ClientVars.translation.firstMenuItem()), maxWidth);
		maxWidth = addMenuItemAndReturnGreatestWidthSoFar(menu, createMenuItem(ClientVars.translation.secondMenuItem()), maxWidth);
		maxWidth = addMenuItemAndReturnGreatestWidthSoFar(menu, createMenuItem(ClientVars.translation.thirdMenuItem()), maxWidth);
		
		// set the wrapper's and parent element's width accordingly
		String newWidth = String.valueOf(maxWidth + 4) + "px"; // 4 for some silent borders (to avoid text wrapping) 
		menu.setWidth(newWidth);
		RootPanel.get("td_leftMenu").setWidth(newWidth);
	}
	
	private void setupMainPageFrame()
	{
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.addStyleName("maxWidth");
		vPanel.addStyleName("maxHeight");
		
		FlowPanel fPanel = new FlowPanel();
		fPanel.addStyleName("maxWidth");
		fPanel.addStyleName("buttonToolbar");
		fPanel.addStyleName("showBorder");
		vPanel.add(fPanel);
		
		// NOTE: must be called before calling "setupKineticJSCanvas"
		// NOTE: must be called before calling the set height method below:
		RootPanel.get("td_mainFrame").add(vPanel);
		
		FocusPanel focusPanel = new FocusPanel();
		focusPanel.addStyleName("maxWidth");
		focusPanel.addStyleName("showBorder");
		focusPanel.setHeight(String.valueOf(vPanel.getOffsetHeight() - fPanel.getOffsetHeight()) + "px");
		vPanel.add(focusPanel);
		
		setupKineticCanvasButtonInterface(fPanel);
		setupKineticJSCanvas(focusPanel);
		
		focusPanel.setFocus(true);
	}
	
	private void setupJSONComparisonPanel()
	{
		jsonComparisonPanel = new PopupPanel(true);
		
		int height = 800;
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setPixelSize(clientWidth, height);
		
		leftTextArea = new TextArea();
		leftTextArea.setPixelSize(clientWidth / 2, height);
		rightTextArea = new TextArea();
		rightTextArea.setPixelSize(clientWidth / 2, height);
		
		hPanel.add(leftTextArea);
		hPanel.add(rightTextArea);
		
		jsonComparisonPanel.add(hPanel);
	}
	
	// ***************************************************************************************************************************
	// additional private interface
	
	private void setLocale(String locale)
	{
		if(Window.confirm(ClientVars.translation.confirm_PageLeaveOrReload()))
		{
			// TODO: detect the user's locale on the server and automatically send the right URL?
			/*
			 * Read the following and decide:
			 * http://www.gwtproject.org/doc/latest/DevGuideI18nLocale.html#LocaleSpecifying  
			 */
			String newURL = GWT.getModuleBaseURL() + "?locale=" + locale;
			Window.Location.replace(newURL);
			
		}
	}
	
	private void setupKineticCanvasButtonInterface(FlowPanel fPanel)
	{
		Button btn_setLeftTA1 = new Button("Set boxes layer (left)", new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				leftTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_BOXES, ClientVars.jsonAttrsToSerialize));
			}
		});
		btn_setLeftTA1.setStyleName("pointCursorOnHover");
		fPanel.add(btn_setLeftTA1);
		
		Button btn_setLeftTA2 = new Button("Set edge layer (left)", new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				// leftTextArea.setText(kineticCanvasManager.getEngine().serializeToMyJSON(EngineComponent.LAYER_EDGES, ClientVars.jsonAttrsToSerialize));
				leftTextArea.setText(kineticCanvas.getEngine().serializeToJSON(EngineComponent.LAYER_EDGES));
			}
		});
		btn_setLeftTA2.setStyleName("pointCursorOnHover");
		fPanel.add(btn_setLeftTA2);
		
		Button btn_setLeftTA3 = new Button("Set serialized edges (left)", new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				leftTextArea.setText(kineticCanvas.getEngine().getEdgeListJSON());
			}
		});
		btn_setLeftTA3.setStyleName("pointCursorOnHover");
		fPanel.add(btn_setLeftTA3);
		
		Button btn_setRightTA = new Button("Set selection (right)", new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				rightTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_SELECTION, ClientVars.jsonAttrsToSerialize));
			}
		});
		btn_setRightTA.setStyleName("pointCursorOnHover");
		fPanel.add(btn_setRightTA);
		
		Button btn_displayComparison = new Button("Display JSON comparison", new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				jsonComparisonPanel.show();
			}
		});
		btn_displayComparison.setStyleName("pointCursorOnHover");
		fPanel.add(btn_displayComparison);
		
		Button btn_printStageJSON = new Button("Print DLayer JSON", new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				System.out.println(kineticCanvas.getEngine().serializeToJSON(EngineComponent.LAYER_BOXES));
			}
		});
		btn_printStageJSON.setStyleName("pointCursorOnHover");
		fPanel.add(btn_printStageJSON);
		
		Button btn_saveStageJSON = new Button("Serialize DLayer", new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				final Storage storage = Storage.getSessionStorageIfSupported();
				if(storage == null)
				{
					Window.alert("Too bad... HTML5 session storage is not supported in your browser!");
				}
				else
				{
					new TempDeselectOperation(kineticCanvas.getEngine(), new Command()
					{
						@Override
						public void execute()
						{
							storage.setItem("DLayerJSON", kineticCanvas.getEngine().serializeToJSON(EngineComponent.LAYER_BOXES));
							storage.setItem("edgeList", kineticCanvas.getEngine().getEdgeListJSON());
						}
					});
				}
			}
		});
		btn_saveStageJSON.setStyleName("pointCursorOnHover");
		fPanel.add(btn_saveStageJSON);
		
		Button btn_loadStageJSON = new Button("Deserialize stage", new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				Storage storage = Storage.getSessionStorageIfSupported();
				if(storage == null)
				{
					Window.alert("Too bad... HTML5 local storage is not supported in your browser!");
				}
				else
				{
					String dLayerJSON = storage.getItem("DLayerJSON");
					String edgeListJSON = storage.getItem("edgeList");
					if(dLayerJSON == null) // || (edgeListJSON == null))
					{
						Window.alert("No stages were saved or your storage is missing some information.");
					}
					else
					{
						if(Window.confirm(ClientVars.translation.confirm_KineticLeaveOrLoad()))
						{
							// kineticCanvas.deserialize(dLayerJSON, edgeListJSON);
						}
					}
				}
			}
		});
		btn_loadStageJSON.setStyleName("pointCursorOnHover");
		fPanel.add(btn_loadStageJSON);
	}
	
	private void setupKineticJSCanvas(FocusPanel panel)
	{
	    kineticCanvas = new KineticEditorCanvas();
	    
	    // TODO: delete this eventually:
	    BoxPrototype b1 = kineticCanvas.getShapeCreator().createBox(NodeRegisterType.MANUAL, "Super box 1", new Vector2d(10, 10), new Vector2d(200, 100));
	    BoxPrototype b2 = kineticCanvas.getShapeCreator().createBox(NodeRegisterType.MANUAL, "Super box 2", new Vector2d(500, 10), new Vector2d(200, 100));
	    BoxPrototype b3 = kineticCanvas.getShapeCreator().createBox(NodeRegisterType.MANUAL, "Super box 3", new Vector2d(400, 300), new Vector2d(200, 100));
	    EdgePrototype e1 = kineticCanvas.getShapeCreator().createEdge(NodeRegisterType.MANUAL, b1, b2);
	    kineticCanvas.getEngine().registerCreated(b1, b2, b3, e1);
	}
	
	private MenuItem createMenuItem(String label)
	{
		MenuItem result = new MenuItem(label, ClientVars.alertNotImplemented);
		result.addStyleName("showBorder");
		return result;
	}
	
	private int addMenuItemAndReturnGreatestWidthSoFar(MenuBar menu, MenuItem menuItem, int currentMaxWidth)
	{
		menu.addItem(menuItem);
		return Math.max(menuItem.getOffsetWidth(), currentMaxWidth);
	}
}
