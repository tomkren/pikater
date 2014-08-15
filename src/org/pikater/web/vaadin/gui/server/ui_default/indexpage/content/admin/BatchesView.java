package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.batches.AllBatchesTableDBView;
import org.pikater.shared.database.views.tableview.batches.BatchTableDBRow;
import org.pikater.shared.database.views.tableview.batches.AbstractBatchTableDBView;
import org.pikater.shared.database.views.tableview.batches.experiments.ExperimentTableDBRow;
import org.pikater.shared.database.views.tableview.batches.experiments.ExperimentTableDBView;
import org.pikater.shared.database.views.tableview.batches.experiments.results.ResultTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.IDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.expandableview.ExpandableView;
import org.pikater.web.vaadin.gui.server.components.dbviews.expandableview.ExpandableViewStep;
import org.pikater.web.vaadin.gui.server.components.dbviews.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithDynamicSteps;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.DynamicNeighbourWizardStep;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

public class BatchesView extends ExpandableView
{
	private static final long serialVersionUID = 5847910505253647132L;

	public BatchesView()
	{
		super();
		setSizeUndefined();
		setWidth("100%");
	}
	
	//----------------------------------------------------
	// VIEW INTERFACE
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		super.finishInit(); // don't forget to!
	}

	@Override
	public boolean hasUnsavedProgress()
	{
		return false;
	}

	@Override
	public String getCloseDialogMessage()
	{
		return null;
	}

	@Override
	protected DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>> getFirstStep()
	{
		return new BatchStep(this, new AllBatchesTableDBView()); 
	}
	
	//----------------------------------------------------------------------------
	// USED VIEW ROOTS
	
	public static class BatchDBViewRoot implements IDBViewRoot<AbstractBatchTableDBView>
	{
		private final AbstractBatchTableDBView dbView;
		
		public BatchDBViewRoot(AbstractBatchTableDBView dbView)
		{
			this.dbView = dbView;
		}

		@Override
		public AbstractBatchTableDBView getUnderlyingDBView()
		{
			return dbView;
		}

		@Override
		public int getColumnSize(ITableColumn column)
		{
			AbstractBatchTableDBView.Column specificColumn = (AbstractBatchTableDBView.Column) column;
			switch(specificColumn)
			{
				case FINISHED:
				case CREATED:
					return 75;
				
				case MAX_PRIORITY:
				case STATUS:
					return 100;
					
				case OWNER:
					return 100;
				case NAME:
					return 125;
				case NOTE:
					return 150;
				case ABORT:
					return 75;
				case RESULTS:
					return 100;
					
				default:
					throw new IllegalStateException("Unknown state: " + specificColumn.name());
			}
		}
		
		@Override
		public ITableColumn getExpandColumn()
		{
			return AbstractBatchTableDBView.Column.NOTE;
		}
		
		@Override
		public void onCellCreate(ITableColumn column, AbstractComponent component)
		{
			AbstractBatchTableDBView.Column specificColumn = (AbstractBatchTableDBView.Column) column;
			if(specificColumn == AbstractBatchTableDBView.Column.NOTE)
			{
				TextField tf_value = (TextField) component;
				tf_value.setDescription(tf_value.getValue());
			}
		}

		@Override
		public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
		{
			// TODO Auto-generated method stub
		}
	}
	
	protected class ExperimentDBViewRoot implements IDBViewRoot<ExperimentTableDBView>
	{
		private final ExperimentTableDBView dbView;
		
		public ExperimentDBViewRoot(JPABatch batch)
		{
			this.dbView = new ExperimentTableDBView(batch.getOwner(), batch);
		}

		@Override
		public ExperimentTableDBView getUnderlyingDBView()
		{
			return dbView;
		}

		@Override
		public int getColumnSize(ITableColumn column)
		{
			ExperimentTableDBView.Column specificColumn = (ExperimentTableDBView.Column) column;
			switch(specificColumn)
			{
				case STATUS:
					return 100;
				
				case STARTED:
				case FINISHED:
					return 75;
				
				case MODEL_STRATEGY:
				case BEST_MODEL:
				case RESULTS:
					return 100;
				
				default:
					throw new IllegalStateException("Unknown state: " + specificColumn.name());
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
	
	protected class ResultDBViewRoot implements IDBViewRoot<ResultTableDBView>
	{
		private final ResultTableDBView dbView;
		
		public ResultDBViewRoot(JPAExperiment experiment)
		{
			this.dbView = new ResultTableDBView(experiment.getBatch().getOwner(), experiment);
		}

		@Override
		public ResultTableDBView getUnderlyingDBView()
		{
			return dbView;
		}
		
		@Override
		public int getColumnSize(ITableColumn column)
		{
			ResultTableDBView.Column specificColumn = (ResultTableDBView.Column) column;
			switch(specificColumn)
			{
				case AGENT_NAME:
				case ERROR_RATE:
				case KAPPA:
				case REL_ABS_ERR:
				case MEAN_ABS_ERR:
					return 100;
					
				case ROOT_REL_SQR_ERR:
				case TRAINED_MODEL:
				case EXPORT:
					return 115;
					
				case WEKA_OPTIONS:
				case NOTE:
				case ROOT_MEAN_SQR_ERR:
					return 125;
				
				default:
					throw new IllegalStateException("Unknown state: " + specificColumn.name());
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
			ResultTableDBView.Column specificColumn = (ResultTableDBView.Column) column;
			if(specificColumn == ResultTableDBView.Column.NOTE)
			{
				TextField tf_value = (TextField) component;
				tf_value.setDescription(tf_value.getValue());				
			}
		}

		@Override
		public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
		{
		}
	}
	
	//----------------------------------------------------------------------------
	// INDIVIDUAL STEPS OF THIS VIEW
	
	protected class BatchStep extends ExpandableViewStep
	{
		private final DBTableLayout innerLayout;
		
		public BatchStep(WizardWithDynamicSteps<IWizardCommon> parentWizard, AbstractBatchTableDBView dbView)
		{
			super(parentWizard, false);
			
			this.innerLayout = new DBTableLayout();
			this.innerLayout.getTable().setMultiSelect(false); // this is required below
			this.innerLayout.setView(new BatchDBViewRoot(dbView));
			registerDBViewLayout(this.innerLayout);
		}

		@Override
		public String getCaption()
		{
			return "Batches overview";
		}

		@Override
		public Component getContent()
		{
			return innerLayout;
		}

		@Override
		public ExpandableViewStep constructNextStep()
		{
			if(!innerLayout.getTable().isARowSelected())
			{
				MyNotifications.showWarning(null, "No table row (batch) is selected.");
				return null;
			}
			else
			{
				// this assumes single select mode
				AbstractTableRowDBView[] selectedRowsViews = innerLayout.getTable().getViewsOfSelectedRows();
				return constructNextStepFromView(selectedRowsViews[0]);
			}
		}

		@Override
		protected ExpandableViewStep constructNextStepFromView(AbstractTableRowDBView view)
		{
			BatchTableDBRow specificView = (BatchTableDBRow) view; 
			return new ExperimentStep(BatchesView.this, specificView.getBatch());
		}
	}
	
	protected class ExperimentStep extends ExpandableViewStep
	{
		private final DBTableLayout innerLayout;
		
		public ExperimentStep(WizardWithDynamicSteps<IWizardCommon> parentWizard, JPABatch batch)
		{
			super(parentWizard, false);
			
			this.innerLayout = new DBTableLayout();
			this.innerLayout.getTable().setMultiSelect(false); // this is required below
			this.innerLayout.setReadOnly(true);
			this.innerLayout.setView(new ExperimentDBViewRoot(batch));
			registerDBViewLayout(this.innerLayout);
		}

		@Override
		public String getCaption()
		{
			return "Experiments overview";
		}

		@Override
		public Component getContent()
		{
			return innerLayout;
		}

		@Override
		public ExpandableViewStep constructNextStep()
		{
			if(!innerLayout.getTable().isARowSelected())
			{
				MyNotifications.showWarning(null, "No table row (experiment) is selected.");
				return null;
			}
			else
			{
				// this assumes single select mode
				AbstractTableRowDBView[] selectedRowsViews = innerLayout.getTable().getViewsOfSelectedRows();
				return constructNextStepFromView(selectedRowsViews[0]);
			}
		}

		@Override
		protected ExpandableViewStep constructNextStepFromView(AbstractTableRowDBView view)
		{
			ExperimentTableDBRow selectedExperimentView = (ExperimentTableDBRow) view;
			return new ResultStep(BatchesView.this, selectedExperimentView.getExperiment());
		}
	}
	
	protected class ResultStep extends ExpandableViewStep
	{
		private final DBTableLayout innerLayout;
		
		public ResultStep(WizardWithDynamicSteps<IWizardCommon> parentWizard, JPAExperiment experiment)
		{
			super(parentWizard, true);
			
			this.innerLayout = new DBTableLayout();
			this.innerLayout.setReadOnly(true);
			this.innerLayout.setView(new ResultDBViewRoot(experiment));
			// registerDBViewLayout(this.innerLayout); // POINTLESS (leaf)
		}

		@Override
		public String getCaption()
		{
			return "Results overview";
		}

		@Override
		public Component getContent()
		{
			return innerLayout;
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