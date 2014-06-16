package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor;

import org.pikater.shared.XStreamHelper;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentState;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor.ExpEditorToolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.KineticComponent;
import org.pikater.web.vaadin.gui.shared.KineticComponentClickMode;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
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
		menu.setStyleName("expEditor-menu");
		
		MenuItem experimentMenuItem = menu.addItem("Experiment", null);
		experimentMenuItem.setStyleName("expEditor-menu-topLevelItem");
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
						saveExperimentInActiveTab(activeComponent);
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
				loadArbitraryExperimentIntoANewTab();
			}
		});
		
		MenuItem settingsMenuItem = menu.addItem("Settings", null);
		settingsMenuItem.setStyleName("expEditor-menu-topLevelItem");
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
		viewMenuItem.setStyleName("expEditor-menu-topLevelItem");
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
		viewMenuItem.addSeparator();
		for(final ExpEditorToolbox toolbox : ExpEditorToolbox.values())
		{
			viewMenuItem.addItem(String.format("Open %s", toolbox.toDisplayName().toLowerCase()), new Command()
			{
				private static final long serialVersionUID = -3102966401422712532L;

				@Override
				public void menuSelected(MenuItem selectedItem)
				{
					parentEditor.openToolbox(toolbox);
				}
			});
		}
		
		if(debugMode)
		{
			MenuItem debugMenuItem = menu.addItem("Debug", null);
			debugMenuItem.setStyleName("expEditor-menu-topLevelItem");
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
		clickModeCB.setEnabled(false);
		
		CheckBox chb_openOptions = new CheckBox("Open options on method select", KineticComponentState.getDefaultOptionsOpenedOnSelection());
		chb_openOptions.addValueChangeListener(new ValueChangeListener()
		{
			private static final long serialVersionUID = -7751913095102968151L;

			@Override
			public void valueChange(final ValueChangeEvent event)
			{
				executeForNonNullActiveTab(new ActionForActiveKineticComponent()
				{
					@Override
					public void doAction(KineticComponent activeComponent)
					{
						activeComponent.getState().openOptionsOnSelection = (Boolean) event.getProperty().getValue();
					}
				}, true);
			}
		});
		
		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.setStyleName("expEditor-toolbar");
		toolbarLayout.setCaption("Instance specific settings and actions:");
		toolbarLayout.setSpacing(true);
		toolbarLayout.addComponent(clickModeLbl);
		toolbarLayout.addComponent(clickModeCB);
		toolbarLayout.setComponentAlignment(clickModeLbl, Alignment.MIDDLE_LEFT);
		toolbarLayout.addComponent(chb_openOptions);
		toolbarLayout.setComponentAlignment(chb_openOptions, Alignment.MIDDLE_LEFT);
		
		addComponent(toolbarLayout);
	}
	
	//---------------------------------------------------------------
	// PUBLIC METHODS
	
	public void onTabSelectionChange(KineticComponent newActiveTabContent)
	{
		if(newActiveTabContent != null)
		{
			clickModeCB.select(newActiveTabContent.getState().clickMode.name());
			clickModeCB.setEnabled(true);
		}
		else
		{
			clickModeCB.setEnabled(false);
		}
	}
	
	public void onClickModeAlteredOnClient(KineticComponentClickMode newClickMode)
	{
		clickModeCB.select(newClickMode.name());
	}
	
	//---------------------------------------------------------------
	// MISCELLANEOUS PRIVATE INTERFACE
	
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
	
	//---------------------------------------------------------------
	// UNTRIVIAL MENU ACTIONS (AFTER CHECKS)
	
	private void saveExperimentInActiveTab(KineticComponent activeComponent)
	{
		if(activeComponent.isContentModified())
		{
			try
			{
				final UniversalComputationDescription uniFormat = activeComponent.exportExperiment();
				MyDialogs.saveExperimentDialog(new MyDialogs.DialogResultHandler()
				{
					@Override
					public boolean handleResult()
					{
						String name = (String) getArg(0);
						Integer userAssignedPriority = (Integer) getArg(1);
						Integer computationEstimateInHours = (Integer) getArg(2);
						Boolean sendEmailWhenFinished = (Boolean) getArg(3);
						String note = (String) getArg(4);
						
						String exportedExperiment = XStreamHelper.serializeToXML(uniFormat, 
								XStreamHelper.getSerializerWithProcessedAnnotations(UniversalComputationDescription.class));
						
						// TODO: finish
						
						JPABatch newExpEntity = new JPABatch(
								name,
								note,
								exportedExperiment,
								ManageAuth.getUserEntity(VaadinSession.getCurrent()),
								userAssignedPriority
						);
						DAOs.batchDAO.storeEntity(newExpEntity);
						return true;
					}
				});
			}
			catch (Throwable t)
			{
				MyNotifications.showError(null, "Experiment could not be saved. Please, contact the administrators.");
			}
		}
		else
		{
			MyNotifications.showWarning("Nothing to save", "The active tab's content is not modified.");
		}
	}
	
	private void loadArbitraryExperimentIntoANewTab()
	{
		// TODO: dialog and experiment chooser
		// parentEditor.loadExperimentIntoNewTab(tabCaption, experiment);
	}
	
	private void setKineticBoxSize(int width)
	{
		parentEditor.getExtension().getClientRPC().command_setBoxSize(width);
		executeForNonNullActiveTab(new ActionForActiveKineticComponent()
		{
			@Override
			public void doAction(KineticComponent activeComponent)
			{
				activeComponent.reloadVisualStyle();
			}
		}, false);
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
