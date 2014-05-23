package org.pikater.web.vaadin.gui.server.components.experimenteditor;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;

public class Toolbar extends VerticalLayout
{
	private static final long serialVersionUID = 3627484964167144046L;
	
	private final ExperimentEditor parentEditor;
	
	public Toolbar(ExperimentEditor parentEditor, boolean debugMode)
	{
		super();
		setSpacing(true);
		setSizeFull();
		
		this.parentEditor = parentEditor;
		
		buildMenuBar(debugMode);
		buildToolbar();
	}
	
	/*
	 * TODO: 
	 * - toolbar state shared with the kinetic component?
	 */
	
	private void buildMenuBar(boolean debugMode)
	{
		MenuBar menu = new MenuBar();
		menu.setSizeFull();
		menu.setStyleName("experiment-editor-menu");
		
		MenuItem experimentMenuItem = menu.addItem("Experiment", null);
		experimentMenuItem.setStyleName("top-level-menu-item");
		experimentMenuItem.addItem("Save active tab...", new Command()
		{
			private static final long serialVersionUID = -8383604249370403859L;

			@Override
			public void menuSelected(MenuItem selectedItem)
			{
				// TODO Auto-generated method stub
				/*
				new TempDeselectOperation(kineticCanvas.getEngine(), new Command()
				{
					@Override
					public void execute()
					{
						// TODO:
						GWTMisc.alertNotImplemented.execute();
						
						server.experimentSchemaSerialized(
								kineticCanvas.getEngine().serializeToJSON(EngineComponent.LAYER_BOXES),
								kineticCanvas.getEngine().getEdgeListJSON()
						);
					}
				});
				*/
			}
		});
		experimentMenuItem.addItem("Load into a new tab...", new Command()
		{
			private static final long serialVersionUID = -2425266907437419549L;

			@Override
			public void menuSelected(MenuItem selectedItem)
			{
				// TODO Auto-generated method stub

				// GWTMisc.alertNotImplemented.execute();
				// kineticState.deserialize(dLayerJSON, edgeListJSON);
				// setFocus(true);
			}
		});
		
		MenuItem settingsMenuItem = menu.addItem("Settings", null);
		settingsMenuItem.setStyleName("top-level-menu-item");
		MenuItem shapeSizeMenuItem = settingsMenuItem.addItem("Shape size (%)", null);
		for(int i = 0; i <= 100; i += 25)
		{
			String caption;
			if(i == 0)
			{
				caption = "0% (minimum reasonable size)";
			}
			else if(i == 100)
			{
				caption = "100% (maximum reasonable size)";
			}
			else
			{
				caption = String.valueOf(i) + '%';
			}
			shapeSizeMenuItem.addItem(caption, new Command()
			{
				private static final long serialVersionUID = 5772970839298315921L;

				@Override
				public void menuSelected(MenuItem selectedItem)
				{
					// TODO Auto-generated method stub
				}
			});
		}
		shapeSizeMenuItem.addSeparator();
		shapeSizeMenuItem.addItem("Scale to browser width", new Command()
		{
			private static final long serialVersionUID = 3151967610233119809L;

			@Override
			public void menuSelected(MenuItem selectedItem)
			{
				// TODO Auto-generated method stub
			}
		});
		
		MenuItem viewMenuItem = menu.addItem("View", null);
		viewMenuItem.setStyleName("top-level-menu-item");
		viewMenuItem.addItem("Go full screen (only FF, Chrome & Opera)", new Command()
		{
			private static final long serialVersionUID = 4109313735302030716L;

			@Override
			public void menuSelected(MenuItem selectedItem)
			{
				// TODO Auto-generated method stub
			}
		});
		
		if(debugMode)
		{
			MenuItem debugMenuItem = menu.addItem("Debug", null);
			debugMenuItem.setStyleName("top-level-menu-item");
			debugMenuItem.addItem("Show debug window", new Command()
			{
				private static final long serialVersionUID = 4109313735302030716L;

				@Override
				public void menuSelected(MenuItem selectedItem)
				{
					// TODO Auto-generated method stub
				}
			});
		}
		
		addComponent(menu);
	}
	
	private void buildToolbar()
	{
		Label clickModeLbl = new Label("Click effect:");
		ComboBox clickModeCB = new ComboBox();
		clickModeCB.addItem("Selection");
		clickModeCB.addItem("Connection");
		clickModeCB.addValueChangeListener(new ValueChangeListener()
		{
			private static final long serialVersionUID = -5032992287714560567L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				// TODO Auto-generated method stub
			}
		});
		
		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.setStyleName("experiment-editor-toolbar");
		toolbarLayout.setCaption("Instance specific settings and actions:");
		toolbarLayout.setSpacing(true);
		toolbarLayout.addComponent(clickModeLbl);
		toolbarLayout.addComponent(clickModeCB);
		toolbarLayout.setComponentAlignment(clickModeLbl, Alignment.MIDDLE_LEFT);
		
		addComponent(toolbarLayout);
	}
	
	/*
	private void build(boolean debugMode)
	{
		if(debugMode)
		{
			Button btn_setLeftTA1 = new Button("Set boxes layer (left)");
			btn_setLeftTA1.addClickListener(new ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					// TODO Auto-generated method stub
					// leftTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_BOXES, GWTMisc.jsonAttrsToSerialize));
				}
			});
			addComponent(btn_setLeftTA1);
			
			Button btn_setLeftTA2 = new Button("Set edge layer (left)");
			btn_setLeftTA2.addClickListener(new ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					// TODO Auto-generated method stub
					// leftTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_EDGES, GWTMisc.jsonAttrsToSerialize));
					// leftTextArea.setText(kineticCanvas.getEngine().serializeToJSON(EngineComponent.LAYER_EDGES));
				}
			});
			addComponent(btn_setLeftTA2);
			
			Button btn_setRightTA = new Button("Set selection (right)");
			btn_setRightTA.addClickListener(new ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					// TODO Auto-generated method stub
					// rightTextArea.setText(kineticCanvas.getEngine().serializeToMyJSON(EngineComponent.LAYER_SELECTION, GWTMisc.jsonAttrsToSerialize));
				}
			});
			addComponent(btn_setRightTA);
			
			Button btn_displayComparison = new Button("Display JSON comparison");
			btn_displayComparison.addClickListener(new ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					// TODO Auto-generated method stub
					// jsonComparisonPanel.show();
				}
			});
			addComponent(btn_displayComparison);
		}
	}
	*/
}
