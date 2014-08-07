package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import java.io.InputStream;
import java.util.UUID;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.pglargeobject.PostgreLobAccess;
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
import org.pikater.web.vaadin.gui.server.components.dbviews.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithDynamicSteps;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.DynamicNeighbourWizardStep;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@StyleSheet("datasetsView.css")
public class DatasetsView extends ExpandableView
{
	private static final long serialVersionUID = -1564809345462937610L;
	
	protected final DataSetTableDBView underlyingView;
	protected final DBTableLayout mainDatasetsLayout;
	
	public DatasetsView()
	{
		super();
		
		this.underlyingView = new DataSetTableDBView();
		
		this.mainDatasetsLayout = new DBTableLayout();
		this.mainDatasetsLayout.setSizeUndefined();
		this.mainDatasetsLayout.getTable().setMultiSelect(false); // this is required below
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		this.mainDatasetsLayout.setCommitImmediately(false);
		
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
		public void onCellCreate(ITableColumn column, AbstractComponent component)
		{
			DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
			if(specificColumn == Column.DESCRIPTION)
			{
				// TODO: set description to the label somehow to display it whole?
			}
		}

		@Override
		public int getColumnSize(ITableColumn column)
		{
			DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
			switch(specificColumn)
			{
				case OWNER:
					return 125;
				
				case DEFAULT_TASK_TYPE:
					return 150;
					
				case NUMBER_OF_INSTANCES:
					return 75;
				
				case CREATED:
					return 100;
				
				case SIZE:
					return 75;
					
				case DESCRIPTION:
					return 300;
					
				case APPROVE:
				case DOWNLOAD:
				case DELETE:
					return 100;
					
				default:
					throw new IllegalStateException(String.format("No sizing information found for column '%s'", specificColumn.name()));
			}
		}

		@Override
		public void approveAction(ITableColumn column, AbstractTableRowDBView row, final Runnable action)
		{
			DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
			if(specificColumn == Column.DOWNLOAD)
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
						return PostgreLobAccess.getPostgreLargeObjectReader(rowView.getDataset().getOID()).getInputStream();
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
						// TODO:
						return null;
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
		public void onCellCreate(ITableColumn column, AbstractComponent component)
		{
		}

		@Override
		public int getColumnSize(ITableColumn column)
		{
			CategoricalMetaDataTableDBView.Column specificColumn = (CategoricalMetaDataTableDBView.Column) column;
			switch(specificColumn)
			{
				case NAME:
					return 100;
				case IS_TARGET:
					return 75;
				case CATEGORY_COUNT:
					return 125;
				case RATIO_OF_MISSING_VALUES:
					return 175;
				case CLASS_ENTROPY:
					return 100;
				case ENTROPY:
					return 100;
				default:
					throw new IllegalStateException(String.format("No sizing information found for column '%s'", specificColumn.name()));
			}
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
		public void onCellCreate(ITableColumn column, AbstractComponent component)
		{
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
				case IS_REAL:
				case MINIMUM:
				case MAXIMUM:
				case AVERAGE:
				// case MODE:
				case MEDIAN:
				case VARIANCE:
					return 75;
				case RATIO_OF_MISSING_VALUES:
					return 175;
				case CLASS_ENTROPY:
					return 100;
				case ENTROPY:
					return 100;
				default:
					throw new IllegalStateException(String.format("No sizing information found for column '%s'", specificColumn.name()));
			}
		}

		@Override
		public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
		{
		}
	}
	
	//----------------------------------------------------------------------------
	// INDIVIDUAL STEPS OF THIS VIEW
	
	protected class DatasetOverviewStep extends DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>>
	{
		public DatasetOverviewStep(WizardWithDynamicSteps<IWizardCommon> parentWizard)
		{
			super(parentWizard, false);
			
			mainDatasetsLayout.getTable().addValueChangeListener(new ValueChangeListener()
			{
				private static final long serialVersionUID = 2787932307187569389L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					getNextButton().setEnabled(!mainDatasetsLayout.getTable().getSelectedRowIDs().isEmpty());
				}
			});
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
		public boolean constructionOfNextStepAllowed()
		{
			boolean result = mainDatasetsLayout.getTable().isARowSelected();
			if(!result)
			{
				MyNotifications.showWarning(null, "No table row (dataset) is selected.");
			}
			return result;
		}

		@Override
		public DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>> constructNextStep()
		{
			AbstractTableRowDBView[] selectedRowsViews = mainDatasetsLayout.getTable().getViewsOfSelectedRows();
			DataSetTableDBRow selectedDatasetView = (DataSetTableDBRow) selectedRowsViews[0]; // this assumes single select mode
			return new DatasetDetailStep(getParentWizard(), selectedDatasetView.getDataset());
		}
	}
	
	protected class DatasetDetailStep extends DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>>
	{
		private final VerticalLayout innerLayout;

		public DatasetDetailStep(WizardWithDynamicSteps<IWizardCommon> parentWizard, JPADataSetLO dataset)
		{
			super(parentWizard, true);
			
			this.innerLayout = new VerticalLayout();
			this.innerLayout.setSizeUndefined();
			this.innerLayout.setSpacing(true);
			this.innerLayout.setStyleName("datasetDetailView");
			
			DBTableLayout categoricalMetadataTable = createTable("Categorical columns:");
			categoricalMetadataTable.setView(new CategoricalMetadataDBViewRoot(new CategoricalMetaDataTableDBView(dataset)));
			categoricalMetadataTable.setReadOnly(true);
			
			DBTableLayout numericalMetadataTable = createTable("Numerical columns:");
			numericalMetadataTable.setView(new NumericalMetadataDBViewRoot(new NumericalMetaDataTableDBView(dataset)));
			numericalMetadataTable.setReadOnly(true);
			
			this.innerLayout.addComponent(categoricalMetadataTable);
			this.innerLayout.addComponent(numericalMetadataTable);
		}
		
		private DBTableLayout createTable(String caption)
		{
			DBTableLayout resultTable = new DBTableLayout();
			resultTable.setCaption(caption);
			resultTable.setSizeUndefined();
			return resultTable;
		}

		@Override
		public String getCaption()
		{
			return "Dataset detail";
		}

		@Override
		public Component getContent()
		{
			return innerLayout;
		}

		@Override
		public boolean constructionOfNextStepAllowed()
		{
			return false;
		}

		@Override
		public DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>> constructNextStep()
		{
			return null;
		}
	}
}