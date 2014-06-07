package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor;

import org.pikater.shared.experiment.webformat.ExperimentMetadata;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.KineticComponent;
import org.pikater.web.vaadin.gui.shared.KineticComponentClickMode;

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
	
	/**
	 * Constant reference to the callback parent.
	 */
	private final ExpEditor parentEditor;
	
	/*
	 * References to dynamic inner components.
	 */
	private ComboBox clickModeCB;
	
	public Toolbar(ExpEditor parentEditor, boolean debugMode)
	{
		super();
		setSpacing(true);
		setSizeFull();
		
		this.parentEditor = parentEditor;
		
		buildMenuBar(debugMode);
		buildToolbar();
	}
	
	//---------------------------------------------------------------
	// INTERFACE BUILDING METHODS
	
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
				executeForNonNullActiveTab(new ActionForActiveKineticComponent()
				{
					@Override
					public void doAction(KineticComponent activeComponent)
					{
						if(activeComponent.isContentModified())
						{
							// TODO: dialog and metadata
							ExperimentMetadata metadata = new ExperimentMetadata("Test");
							activeComponent.getClientRPC().request_sendExperimentToSave(metadata);
						}
						else
						{
							MyNotifications.showWarning("Nothing to save", "The active tab's content is not modified");
						}
					}
				}, true);
			}
		});
		experimentMenuItem.addItem("Load into a new tab...", new Command()
		{
			private static final long serialVersionUID = -2425266907437419549L;

			@Override
			public void menuSelected(MenuItem selectedItem)
			{
				// TODO: dialog and experiment chooser
				// parentEditor.getActiveKineticComponent().getClientRPC().command_receiveExperimentToLoad(experiment);
				MyNotifications.showWarning(null, "Not implemented yet.");
			}
		});
		
		MenuItem settingsMenuItem = menu.addItem("Settings", null);
		settingsMenuItem.setStyleName("top-level-menu-item");
		MenuItem shapeSizeMenuItem = settingsMenuItem.addItem("Shape size (%)", null);
		for(int percent = 0; percent <= 100; percent += 25)
		{
			String caption;
			if(percent == 0)
			{
				caption = "0% (minimum reasonable size)";
			}
			else if(percent == 100)
			{
				caption = "100% (maximum reasonable size)";
			}
			else
			{
				caption = String.valueOf(percent) + '%';
			}
			final int finalPercentCopy = percent;
			shapeSizeMenuItem.addItem(caption, new Command()
			{
				private static final long serialVersionUID = 5772970839298315921L;

				@Override
				public void menuSelected(MenuItem selectedItem)
				{
					setKineticBoxSize(finalPercentCopy);
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
				setKineticBoxSize(-1);
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
				// TODO:
				MyNotifications.showWarning(null, "Not implemented yet.");
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
					// TODO:
					MyNotifications.showWarning(null, "Not implemented yet.");
				}
			});
		}
		
		addComponent(menu);
	}
	
	private void buildToolbar()
	{
		Label clickModeLbl = new Label("Click effect:");
		clickModeCB = new ComboBox();
		for(KineticComponentClickMode clickMode : KineticComponentClickMode.values())
		{
			clickModeCB.addItem(clickMode.name());
		}
		clickModeCB.setImmediate(true);
		clickModeCB.setNullSelectionAllowed(false);
		clickModeCB.setTextInputAllowed(false);
		clickModeCB.addValueChangeListener(new ValueChangeListener()
		{
			private static final long serialVersionUID = -5032992287714560567L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				executeForNonNullActiveTab(new ActionForActiveKineticComponent()
				{
					@Override
					public void doAction(KineticComponent activeComponent)
					{
						activeComponent.getState().clickMode = KineticComponentClickMode.valueOf((String) clickModeCB.getValue());
					}
				}, true);
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
	
	//---------------------------------------------------------------
	// PUBLIC METHODS
	
	public void onTabSelectionChange(KineticComponent newActiveTabContent)
	{
		if(newActiveTabContent != null)
		{
			clickModeCB.select(newActiveTabContent.getState().clickMode.name());
		}
	}
	
	public void onClickModeAlteredOnClient(KineticComponentClickMode newClickMode)
	{
		clickModeCB.select(newClickMode.name());
	}
	
	//---------------------------------------------------------------
	// PRIVATE METHODS
	
	private void setKineticBoxSize(int width)
	{
		parentEditor.getExtension().getClientRPC().command_setBoxSize(width);
		executeForNonNullActiveTab(new ActionForActiveKineticComponent()
		{
			@Override
			public void doAction(KineticComponent activeComponent)
			{
				activeComponent.getClientRPC().request_reloadVisualStyle();
			}
		}, false);
	}
	
	private void executeForNonNullActiveTab(ActionForActiveKineticComponent action, boolean displayWarningIfNull)
	{
		KineticComponent activeComponent = parentEditor.getActiveKineticComponent();
		if(activeComponent != null)
		{
			action.doAction(activeComponent);
		}
		else if(displayWarningIfNull)
		{
			MyNotifications.showWarning(null, "No tabs have been created.");
		}
	}
	
	//---------------------------------------------------------------
	// PRIVATE TYPES
	
	private interface ActionForActiveKineticComponent
	{
		void doAction(KineticComponent activeComponent);
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
					// jsonComparisonPanel.show();
				}
			});
			addComponent(btn_displayComparison);
		}
	}
	*/
}
