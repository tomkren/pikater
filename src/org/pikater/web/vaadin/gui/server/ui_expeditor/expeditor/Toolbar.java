package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor;

import java.util.List;

import org.pikater.core.agents.gateway.WebToCoreEntryPoint;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.batches.BatchTableDBRow;
import org.pikater.shared.database.views.tableview.batches.UserSavedBatchesTableDBView;
import org.pikater.shared.database.views.tableview.batches.UserScheduledBatchesTableDBView;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.dbviews.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.forms.SaveExperimentForm;
import org.pikater.web.vaadin.gui.server.components.forms.SaveExperimentForm.ExperimentSaveMode;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.DialogCommons;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.DialogCommons.IDialogComponent;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.SpecialDialogs;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.BatchesView.BatchDBViewRoot;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor.ExpEditorToolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.KineticComponent;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.KineticComponent.IOnExperimentSaved;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.ClickMode;

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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import de.steinwedel.messagebox.MessageBox;

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
	
	public void onClickModeAlteredOnClient(ClickMode newClickMode)
	{
		clickModeCB.select(newClickMode.name());
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
		experimentMenuItem.addItem("Save experiment from active tab...", new Command()
		{
			private static final long serialVersionUID = -8383604249370403859L;

			@Override
			public void menuSelected(MenuItem selectedItem)
			{
				executeForNonNullActiveTab(new IActiveKineticComponentAction()
				{
					@Override
					public void doAction(KineticComponent activeComponent)
					{
						saveExperimentInActiveTab(activeComponent, ExperimentSaveMode.SAVE_FOR_LATER);
					}
				}, true);
			}
		});
		experimentMenuItem.addItem("Schedule experiment from active tab...", new Command()
		{
			private static final long serialVersionUID = -8383604249370403859L;

			@Override
			public void menuSelected(MenuItem selectedItem)
			{
				executeForNonNullActiveTab(new IActiveKineticComponentAction()
				{
					@Override
					public void doAction(KineticComponent activeComponent)
					{
						saveExperimentInActiveTab(activeComponent, ExperimentSaveMode.SAVE_FOR_EXECUTION);
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
		for(ClickMode clickMode : ClickMode.values())
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
				executeForNonNullActiveTab(new IActiveKineticComponentAction()
				{
					@Override
					public void doAction(KineticComponent activeComponent)
					{
						activeComponent.getState().clickMode = ClickMode.valueOf((String) clickModeCB.getValue());
					}
				}, true);
			}
		});
		clickModeCB.setEnabled(false);
		
		CheckBox chb_openOptions = new CheckBox("bind options manager with selection changes", KineticComponent.areSelectionChangesBoundWithOptionsManagerByDefault());
		chb_openOptions.addValueChangeListener(new ValueChangeListener()
		{
			private static final long serialVersionUID = -7751913095102968151L;

			@Override
			public void valueChange(final ValueChangeEvent event)
			{
				executeForNonNullActiveTab(new IActiveKineticComponentAction()
				{
					@Override
					public void doAction(KineticComponent activeComponent)
					{
						activeComponent.setBindOptionsManagerWithSelectionChanges((Boolean) event.getProperty().getValue());
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
	// UNTRIVIAL MENU ACTIONS (AFTER CHECKS)
	
	private void saveExperimentInActiveTab(final KineticComponent activeComponent, final ExperimentSaveMode saveMode)
	{
		try
		{
			activeComponent.exportExperiment(new KineticComponent.IOnExperimentExported()
			{
				private JPAUser experimentOwner;
				private JPABatch sourceExperiment;
				private String experimentXML;
				
				@Override
				public void handleExperiment(final UniversalComputationDescription exportedExperiment, final IOnExperimentSaved experimentSavedCallback)
				{
					// first check for the right conditions
					switch(saveMode)
					{
						case SAVE_FOR_LATER:
							if(!activeComponent.isContentModified())
							{
								MyNotifications.showInfo("Nothing to save", "Content is not modified.");
								return;
							}
							break;
						case SAVE_FOR_EXECUTION:
							if(!exportedExperiment.isValid())
							{
								MyNotifications.showInfo("Experiment not valid", "Can not schedule it.");
								return;
							}
							break;
						default:
							throw new IllegalStateException("Unknown state: " + saveMode.name());
					}
					
					// and then actually do something
					final boolean sourceExperimentExists = activeComponent.getPreviouslyLoadedExperimentID() != null;
					if(sourceExperimentExists)
					{
						sourceExperiment = DAOs.batchDAO.getByID(activeComponent.getPreviouslyLoadedExperimentID());
					}
					else
					{
						sourceExperiment = null;
					}
					final SaveExperimentForm seForm = new SaveExperimentForm(saveMode, sourceExperiment);
					MessageBox dialog = SpecialDialogs.saveExperimentDialog(seForm, new DialogCommons.IDialogResultHandler()
					{
						@Override
						public boolean handleResult(Object[] args)
						{
							experimentOwner = ManageAuth.getUserEntity(VaadinSession.getCurrent());
							experimentXML = exportedExperiment.toXML();
							switch(saveMode)
							{
								case SAVE_FOR_LATER:
									String name = (String) args[0];
									String note = (String) args[1];
									switch(seForm.getSaveForLaterMode())
									{
										case SAVE_AS_NEW:
											saveExperiment(new JPABatch(name, note, experimentXML, experimentOwner), experimentSavedCallback);
											break;
										case OVERWRITE_PREVIOUS:
											sourceExperiment.setName(name);
											sourceExperiment.setNote(note);
											sourceExperiment.setXML(experimentXML);
											updateExperiment(sourceExperiment, experimentSavedCallback);
											break;
										case SAVE_AS_NEW_AND_DELETE_PREVIOUS:
											saveExperiment(new JPABatch(name, note, experimentXML, experimentOwner), experimentSavedCallback);
											if(sourceExperimentExists)
											{
												DAOs.batchDAO.deleteBatchEntity(sourceExperiment);
											}
											break;
										default:
											throw new IllegalStateException("Unknown state: " + seForm.getSaveForLaterMode().name());
									}
									return true;
								case SAVE_FOR_EXECUTION:
									saveForExecution(args, experimentSavedCallback);
									return true;
								default:
									throw new IllegalStateException("Unknown state: " + saveMode.name());
							}
						}
					});
					dialog.setWidth("400px");
				}
				
				private void saveForExecution(Object[] args, IOnExperimentSaved experimentSavedCallback)
				{
					String name = (String) args[0];
					Integer userAssignedPriority = (Integer) args[1];
					Boolean sendEmailWhenFinished = (Boolean) args[2];
					String note = (String) args[3];
					
					JPABatch newExperiment = new JPABatch(name, note, experimentXML, experimentOwner, userAssignedPriority, sendEmailWhenFinished);
					saveExperiment(newExperiment, experimentSavedCallback);
					try
					{
						WebToCoreEntryPoint.notify_newBatch(newExperiment.getId());
					}
					catch (PikaterGatewayException e)
					{
						// TODO: delete the newly saved batch
						PikaterLogger.logThrowable("Could not send notification about a new batch to core.", e);
						MyNotifications.showError("Failed", "Saved but not scheduled.");
					}
				}
				
				private void saveExperiment(JPABatch newExperiment, IOnExperimentSaved experimentSavedCallback)
				{
					DAOs.batchDAO.storeEntity(newExperiment);
					experimentSavedCallback.experimentSaved(newExperiment);
				}
				
				private void updateExperiment(JPABatch experiment, IOnExperimentSaved experimentSavedCallback)
				{
					DAOs.batchDAO.updateEntity(experiment);
					experimentSavedCallback.experimentSaved(experiment);
				}
			});
		}
		catch (Throwable t)
		{
			PikaterLogger.logThrowable("Could not save experiment", t);
			MyNotifications.showApplicationError();
		}
	}
	
	private void loadArbitraryExperimentIntoANewTab()
	{
		MessageBox dialog = SpecialDialogs.loadExperimentDialog(new LoadExperimentComponent()
		{
			private static final long serialVersionUID = -3405189166030944018L;

			@Override
			public boolean handleResult(Object[] args)
			{
				parentEditor.loadExperimentIntoNewTab((JPABatch) args[0]); 
				return true;
			}
		});
		dialog.setWidth("600px");
	}
	
	private void setKineticBoxSize(int width)
	{
		parentEditor.getExtension().getClientRPC().command_setBoxSize(width);
		executeForNonNullActiveTab(new IActiveKineticComponentAction()
		{
			@Override
			public void doAction(KineticComponent activeComponent)
			{
				activeComponent.reloadVisualStyle();
			}
		}, false);
	}
	
	//---------------------------------------------------------------
	// MISCELLANEOUS PRIVATE INTERFACE
	
	private void executeForNonNullActiveTab(IActiveKineticComponentAction action, boolean displayWarningIfNull)
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
	
	private interface IActiveKineticComponentAction
	{
		void doAction(KineticComponent activeComponent);
	}
	
	private abstract class LoadExperimentComponent extends TabSheet implements IDialogComponent
	{
		private static final long serialVersionUID = -7500075637433995569L;
		
		private JPABatch experimentToLoad;
		
		public LoadExperimentComponent()
		{
			super();
			setStyleName("loadExperimentDialogContent");
			
			JPAUser currentUser = ManageAuth.getUserEntity(VaadinSession.getCurrent());
			
			final DBTableLayout savedExperimentsLayout = new DBTableLayout();
			savedExperimentsLayout.setSizeFull();
			savedExperimentsLayout.setReadOnly(true);
			savedExperimentsLayout.getTable().setMultiSelect(false); // required below
			savedExperimentsLayout.getTable().addValueChangeListener(new ValueChangeListener()
			{
				private static final long serialVersionUID = -2896004727434158169L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					/*
					 * Table in 'savedExperimentsLayout' is required to have multi-select disabled.
					 */
					AbstractTableRowDBView[] selectedRowViews = savedExperimentsLayout.getTable().getViewsOfSelectedRows();
					if(selectedRowViews.length == 0)
					{
						experimentToLoad = null;
					}
					else
					{
						experimentToLoad = ((BatchTableDBRow) selectedRowViews[0]).getBatch();
					}
				}
			});
			savedExperimentsLayout.setView(new BatchDBViewRoot(new UserSavedBatchesTableDBView(currentUser)));
			
			final DBTableLayout scheduledExperimentsLayout = new DBTableLayout();
			scheduledExperimentsLayout.setSizeFull();
			scheduledExperimentsLayout.setReadOnly(true);
			scheduledExperimentsLayout.getTable().setMultiSelect(false); // required below
			scheduledExperimentsLayout.getTable().addValueChangeListener(new ValueChangeListener()
			{
				private static final long serialVersionUID = 5024393452584477476L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					/*
					 * Table in 'scheduledExperimentsLayout' is required to have multi-select disabled.
					 */
					AbstractTableRowDBView[] selectedRowViews = scheduledExperimentsLayout.getTable().getViewsOfSelectedRows();
					if(selectedRowViews.length == 0)
					{
						experimentToLoad = null;
					}
					else
					{
						experimentToLoad = ((BatchTableDBRow) selectedRowViews[0]).getBatch();
					}
				}
			});
			scheduledExperimentsLayout.setView(new BatchDBViewRoot(new UserScheduledBatchesTableDBView(currentUser)));
			
			addTab(savedExperimentsLayout, "Saved experiments");
			addTab(scheduledExperimentsLayout, "Scheduled experiments");
			
			this.experimentToLoad = null;
		}

		@Override
		public boolean isResultReadyToBeHandled()
		{
			return experimentToLoad != null;
		}
		
		@Override
		public void addArgs(List<Object> arguments)
		{
			arguments.add(experimentToLoad);
		}
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
