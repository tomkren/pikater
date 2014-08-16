package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.postgre.largeobject.PGLargeObjectReader;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBRow;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBView;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBView.Column;
import org.pikater.shared.database.views.tableview.datasets.metadata.CategoricalMetaDataTableDBView;
import org.pikater.shared.database.views.tableview.datasets.metadata.NumericalMetaDataTableDBView;
import org.pikater.web.HttpContentType;
import org.pikater.web.sharedresources.ResourceExpiration;
import org.pikater.web.sharedresources.ResourceRegistrar;
import org.pikater.web.sharedresources.download.IDownloadResource;
import org.pikater.web.vaadin.gui.server.components.dbviews.IDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.expandableview.ExpandableView;
import org.pikater.web.vaadin.gui.server.components.dbviews.expandableview.ExpandableViewStep;
import org.pikater.web.vaadin.gui.server.components.dbviews.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithDynamicSteps;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.DynamicNeighbourWizardStep;
import org.pikater.web.vaadin.gui.server.layouts.SimplePanel;
import org.pikater.web.vaadin.gui.server.ui_visualization.VisualizationUI.DSVisOneUIArgs;
import org.pikater.web.visualisation.DatasetVisualizationEntryPoint;
import org.pikater.web.visualisation.definition.result.DSVisOneResult;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;

@StyleSheet("datasetsView.css")
public class DatasetsView extends ExpandableView
{
	private static final long serialVersionUID = -1564809345462937610L;
	
	protected final DataSetTableDBView underlyingView;
	protected final DBTableLayout mainDatasetsLayout;
	
	public DatasetsView()
	{
		super();
		setSizeUndefined();
		setWidth("100%");
		
		this.underlyingView = new DataSetTableDBView();
		
		this.mainDatasetsLayout = new DBTableLayout();
		this.mainDatasetsLayout.setSizeFull();
		this.mainDatasetsLayout.getTable().setMultiSelect(false); // this is required below
	}
	
	//----------------------------------------------------
	// VIEW INTERFACE

	@Override
	public void enter(ViewChangeEvent event)
	{
		// always call these last, when you're absolutely ready to display the content
		this.mainDatasetsLayout.setView(new DatasetDBViewRoot());
		super.finishInit(); // don't forget to!
	}

	@Override
	public boolean hasUnsavedProgress()
	{
		return false; // datasets are completely read-only, except for adding new datasets which is independent of Vaadin anyway
	}

	@Override
	public String getCloseDialogMessage()
	{
		return null;
	}
	
	@Override
	protected DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>> getFirstStep()
	{
		// this is called in the constructor via super
		return new DatasetOverviewStep(this);
	}
	
	//----------------------------------------------------------------------------
	// USED VIEW ROOTS
	
	protected class DatasetDBViewRoot implements IDBViewRoot<DataSetTableDBView>
	{
		public DatasetDBViewRoot()
		{
		}
		
		@Override
		public DataSetTableDBView getUnderlyingDBView()
		{
			return underlyingView;
		}

		@Override
		public int getColumnSize(ITableColumn column)
		{
			DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
			switch(specificColumn)
			{
				case OWNER:
					return 125;
					
				case CREATED:
					return 100;
					
				case SIZE:
					return 60;
				
				case DEFAULT_TASK_TYPE:
					return 100;
					
				case NUMBER_OF_INSTANCES:
					return 75;
				
				case DESCRIPTION:
					return 300;
					
				case APPROVED:
					return 75;
					
				case VISUALIZE:
				case DOWNLOAD:
					return 100;
				case DELETE:
					return 75;
					
				default:
					throw new IllegalStateException(String.format("No sizing information found for column '%s'", specificColumn.name()));
			}
		}
		
		@Override
		public ITableColumn getExpandColumn()
		{
			return DataSetTableDBView.Column.DESCRIPTION;
		}
		
		@Override
		public void onCellCreate(ITableColumn column, AbstractComponent component)
		{
			DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
			if(specificColumn == Column.DESCRIPTION)
			{
				TextField tf_value = (TextField) component;
				tf_value.setDescription(tf_value.getValue());
			}
		}

		@Override
		public void approveAction(ITableColumn column, AbstractTableRowDBView row, final Runnable action)
		{
			DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
			if(specificColumn == Column.VISUALIZE)
			{
				// determine arguments
				final JPADataSetLO dataset = ((DataSetTableDBRow) row).getDataset(); 
				
				// TODO: dialog
				final List<String> attrNames = new ArrayList<String>(); 
				for(JPAAttributeMetaData attrMetaData : dataset.getAttributeMetaData())
				{
					attrNames.add(attrMetaData.getName());
				}
				final String attrTarget = dataset.getAttributeMetaData().get(dataset.getNumberOfAttributes() - 1).getName();
				
				// show progress dialog
				ProgressDialog.show("Vizualization progress...", new ProgressDialog.IProgressDialogTaskHandler()
				{
					private DatasetVisualizationEntryPoint underlyingTask;

					@Override
					public void startTask(IProgressDialogResultHandler contextForTask) throws Throwable
					{
						// start the task and bind it with the progress dialog
						underlyingTask = new DatasetVisualizationEntryPoint(contextForTask);
						underlyingTask.visualizeDataset(
								dataset,
								attrNames.toArray(new String[0]),
								attrTarget
						);
					}

					@Override
					public void abortTask()
					{
						underlyingTask.abortVisualization();
					}

					@Override
					public void onTaskFinish(IProgressDialogTaskResult result)
					{
						// and when the task finishes, construct the UI
						DSVisOneUIArgs uiArgs = new DSVisOneUIArgs((DSVisOneResult) result); 
						Page.getCurrent().setLocation(uiArgs.toRedirectURL());
					}
				});
			}
			else if(specificColumn == Column.DOWNLOAD)
			{
				// download, don't run action
				final DataSetTableDBRow rowView = (DataSetTableDBRow) row;
				final UUID datasetDownloadResourceUI = ResourceRegistrar.registerResource(VaadinSession.getCurrent(), new IDownloadResource()
				{
					@Override
					public ResourceExpiration getLifeSpan()
					{
						return ResourceExpiration.ON_FIRST_PICKUP;
					}
					
					@Override
					public InputStream getStream() throws Throwable
					{
						return PGLargeObjectReader.getForLargeObject(rowView.getDataset().getOID()).getInputStream();
					}
					
					@Override
					public long getSize()
					{
						return rowView.getDataset().getSize();
					}
					
					@Override
					public String getMimeType()
					{
						return HttpContentType.APPLICATION_JAR.toString();
					}
					
					@Override
					public String getFilename()
					{
						return DAOs.filemappingDAO.getSingleExternalFilename(rowView.getDataset());
					}
				});
				Page.getCurrent().setLocation(ResourceRegistrar.getDownloadURL(datasetDownloadResourceUI));
			}
			else if(specificColumn == Column.DELETE)
			{
				GeneralDialogs.confirm(null, "Really delete this dataset?", new GeneralDialogs.IDialogResultHandler()
				{
					@Override
					public boolean handleResult(Object[] args)
					{
						action.run(); // approve
						return true;
					}
				});
			}
			else
			{
				// always approve
				action.run();
			}
		}
	}
	
	protected class CategoricalMetadataDBViewRoot implements IDBViewRoot<CategoricalMetaDataTableDBView>
	{
		private final CategoricalMetaDataTableDBView dbView;
		
		public CategoricalMetadataDBViewRoot(CategoricalMetaDataTableDBView dbView)
		{
			this.dbView = dbView;
		}

		@Override
		public CategoricalMetaDataTableDBView getUnderlyingDBView()
		{
			return dbView;
		}

		@Override
		public int getColumnSize(ITableColumn column)
		{
			CategoricalMetaDataTableDBView.Column specificColumn = (CategoricalMetaDataTableDBView.Column) column;
			switch(specificColumn)
			{
				case NAME:
					return 150;
				case IS_TARGET:
					return 75;
				case CATEGORY_COUNT:
					return 100;
				case RATIO_OF_MISSING_VALUES:
					return 125;
				case CLASS_ENTROPY:
					return 75;
				case ENTROPY:
					return 115;
				default:
					throw new IllegalStateException(String.format("No sizing information found for column '%s'", specificColumn.name()));
			}
		}
		
		@Override
		public ITableColumn getExpandColumn()
		{
			return null;
		}
		
		@Override
		public void onCellCreate(ITableColumn column, AbstractComponent component)
		{
		}

		@Override
		public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
		{
		}
	}
	
	protected class NumericalMetadataDBViewRoot implements IDBViewRoot<NumericalMetaDataTableDBView>
	{
		private final NumericalMetaDataTableDBView dbView;
		
		public NumericalMetadataDBViewRoot(NumericalMetaDataTableDBView dbView)
		{
			this.dbView = dbView;
		}

		@Override
		public NumericalMetaDataTableDBView getUnderlyingDBView()
		{
			return dbView;
		}

		@Override
		public int getColumnSize(ITableColumn column)
		{
			NumericalMetaDataTableDBView.Column specificColumn = (NumericalMetaDataTableDBView.Column) column;
			switch(specificColumn)
			{
				case NAME:
					return 100;
				case IS_TARGET:
					return 75;
				
				case IS_REAL:
					return 55;
					
				case MINIMUM:
				case MAXIMUM:
				case AVERAGE:
				case MEDIAN:
				// case MODE:
					return 65;
				
				case VARIANCE:
					return 70;
				case RATIO_OF_MISSING_VALUES:
					return 125;
				case ENTROPY:
					return 70;
				case CLASS_ENTROPY:
					return 100;
				
				default:
					throw new IllegalStateException(String.format("No sizing information found for column '%s'", specificColumn.name()));
			}
		}
		
		@Override
		public ITableColumn getExpandColumn()
		{
			return NumericalMetaDataTableDBView.Column.CLASS_ENTROPY;
		}
		
		@Override
		public void onCellCreate(ITableColumn column, AbstractComponent component)
		{
		}

		@Override
		public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
		{
		}
	}
	
	//----------------------------------------------------------------------------
	// INDIVIDUAL STEPS OF THIS VIEW
	
	protected class DatasetOverviewStep extends ExpandableViewStep
	{
		public DatasetOverviewStep(WizardWithDynamicSteps<IWizardCommon> parentWizard)
		{
			super(parentWizard, false);
			
			registerDBViewLayout(mainDatasetsLayout);
		}

		@Override
		public String getCaption()
		{
			return "Datasets overview";
		}

		@Override
		public DBTableLayout getContent()
		{
			return mainDatasetsLayout;
		}

		@Override
		public ExpandableViewStep constructNextStep()
		{
			if(!mainDatasetsLayout.getTable().isARowSelected())
			{
				MyNotifications.showWarning(null, "No table row (dataset) is selected.");
				return null;
			}
			else
			{
				// this assumes single select mode
				AbstractTableRowDBView[] selectedRowsViews = mainDatasetsLayout.getTable().getViewsOfSelectedRows();
				return constructNextStepFromView(selectedRowsViews[0]);
			}
		}

		@Override
		protected ExpandableViewStep constructNextStepFromView(AbstractTableRowDBView view)
		{
			DataSetTableDBRow selectedDatasetView = (DataSetTableDBRow) view;
			return new DatasetDetailStep(getParentWizard(), selectedDatasetView.getDataset());
		}
	}
	
	protected class DatasetDetailStep extends ExpandableViewStep
	{
		private final SimplePanel panel;

		public DatasetDetailStep(WizardWithDynamicSteps<IWizardCommon> parentWizard, JPADataSetLO dataset)
		{
			super(parentWizard, true);
			
			DBTableLayout categoricalMetadataTable = new DBTableLayout();
			categoricalMetadataTable.setView(new CategoricalMetadataDBViewRoot(new CategoricalMetaDataTableDBView(dataset)));
			categoricalMetadataTable.setReadOnly(true);
			
			DBTableLayout numericalMetadataTable = new DBTableLayout();
			numericalMetadataTable.setView(new NumericalMetadataDBViewRoot(new NumericalMetaDataTableDBView(dataset)));
			numericalMetadataTable.setReadOnly(true);
			
			TabSheet tabSheet = new TabSheet();
			tabSheet.setSizeFull();
			tabSheet.setImmediate(true);
			tabSheet.addTab(categoricalMetadataTable, "Categorical");
			tabSheet.addTab(numericalMetadataTable, "Numerical");
			
			this.panel = new SimplePanel(tabSheet);
			this.panel.addStyleName("datasetDetailView");
			this.panel.setSizeFull();
		}
		
		@Override
		public String getCaption()
		{
			return "Dataset metadata";
		}

		@Override
		public Component getContent()
		{
			return this.panel;
		}

		@Override
		public ExpandableViewStep constructNextStep()
		{
			return null;
		}

		@Override
		protected ExpandableViewStep constructNextStepFromView(AbstractTableRowDBView view)
		{
			return null;
		}
	}
}