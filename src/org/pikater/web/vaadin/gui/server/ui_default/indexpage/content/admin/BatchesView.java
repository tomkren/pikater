package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.batches.BatchTableDBRow;
import org.pikater.shared.database.views.tableview.batches.BatchTableDBView;
import org.pikater.shared.database.views.tableview.batches.experiments.ExperimentTableDBRow;
import org.pikater.shared.database.views.tableview.batches.experiments.ExperimentTableDBView;
import org.pikater.shared.database.views.tableview.batches.experiments.results.ResultTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.IDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.expandableview.ExpandableView;
import org.pikater.web.vaadin.gui.server.components.dbviews.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithDynamicSteps;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.DynamicNeighbourWizardStep;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;

public class BatchesView extends ExpandableView
{
	private static final long serialVersionUID = 5847910505253647132L;

	public BatchesView()
	{
		super();
	}

	@Override
	public boolean hasUnsavedProgress()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCloseDialogMessage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		super.finishInit(); // don't forget to!
	}

	@Override
	protected DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>> getFirstStep()
	{
		return new BatchStep(this, null); 
	}
	
	//----------------------------------------------------------------------------
	// USED VIEW ROOTS
	
	protected class BatchDBViewRoot implements IDBViewRoot<BatchTableDBView>
	{
		private final BatchTableDBView dbView;
		
		public BatchDBViewRoot(JPAUser user)
		{
			this.dbView = new BatchTableDBView(user);
		}

		@Override
		public BatchTableDBView getUnderlyingDBView()
		{
			return dbView;
		}

		@Override
		public void onCellCreate(ITableColumn column, Object component)
		{
			// TODO: set tooltip to NOTE
		}

		@Override
		public int getColumnSize(ITableColumn column)
		{
			BatchTableDBView.Column specificColumn = (BatchTableDBView.Column) column;
			switch(specificColumn)
			{
				case FINISHED:
				case STATUS:
				case PRIORITY:
				case MAX_PRIORITY:
				case CREATED:
					return 75;
				case OWNER:
				case NAME:
					return 100;
				case NOTE:
					return 200;
				default:
					throw new IllegalStateException("Unknown state: " + specificColumn.name());
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
		public void onCellCreate(ITableColumn column, Object component)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public int getColumnSize(ITableColumn column)
		{
			// TODO Auto-generated method stub
			return 100;
		}

		@Override
		public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
		{
			// TODO Auto-generated method stub
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
		public void onCellCreate(ITableColumn column, Object component)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public int getColumnSize(ITableColumn column)
		{
			// TODO Auto-generated method stub
			return 100;
		}

		@Override
		public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
		{
			// TODO Auto-generated method stub
		}
	}
	
	//----------------------------------------------------------------------------
	// INDIVIDUAL STEPS OF THIS VIEW
	
	protected class BatchStep extends DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>>
	{
		private final DBTableLayout innerLayout;
		
		public BatchStep(WizardWithDynamicSteps<IWizardCommon> parentWizard, JPAUser user)
		{
			super(parentWizard, false);
			
			// some basic setup
			this.innerLayout = new DBTableLayout();
			this.innerLayout.setSizeUndefined();
			this.innerLayout.getTable().setMultiSelect(false); // this is required below
			// this.innerLayout.setCommitImmediately(false);
			
			// view setup
			this.innerLayout.setView(new BatchDBViewRoot(user));
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
		public boolean constructionOfNextStepAllowed()
		{
			boolean result = innerLayout.getTable().isARowSelected();
			if(!result)
			{
				MyNotifications.showWarning(null, "No table row (batch) is selected.");
			}
			return result;
		}

		@Override
		public DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>> constructNextStep()
		{
			AbstractTableRowDBView[] selectedRowsViews = innerLayout.getTable().getViewsOfSelectedRows();
			BatchTableDBRow selectedBatchView = (BatchTableDBRow) selectedRowsViews[0]; // this assumes single select mode
			return new ExperimentStep(BatchesView.this, selectedBatchView.getBatch());
		}
	}
	
	protected class ExperimentStep extends DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>>
	{
		private final DBTableLayout innerLayout;
		
		public ExperimentStep(WizardWithDynamicSteps<IWizardCommon> parentWizard, JPABatch batch)
		{
			super(parentWizard, false);
			
			// some basic init
			this.innerLayout = new DBTableLayout();
			this.innerLayout.setSizeUndefined();
			this.innerLayout.getTable().setMultiSelect(false); // this is required below
			this.innerLayout.setCommitImmediately(false);

			// view setup
			this.innerLayout.setView(new ExperimentDBViewRoot(batch));
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
		public boolean constructionOfNextStepAllowed()
		{
			boolean result = innerLayout.getTable().isARowSelected();
			if(!result)
			{
				MyNotifications.showWarning(null, "No table row (experiment) is selected.");
			}
			return result;
		}

		@Override
		public DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>> constructNextStep()
		{
			AbstractTableRowDBView[] selectedRowsViews = innerLayout.getTable().getViewsOfSelectedRows();
			ExperimentTableDBRow selectedExperimentView = (ExperimentTableDBRow) selectedRowsViews[0]; // this assumes single select mode
			return new ResultStep(BatchesView.this, selectedExperimentView.getExperiment());
		}
	}
	
	protected class ResultStep extends DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>>
	{
		private final DBTableLayout innerLayout;
		
		public ResultStep(WizardWithDynamicSteps<IWizardCommon> parentWizard, JPAExperiment experiment)
		{
			super(parentWizard, true);
			
			// some basic init
			this.innerLayout = new DBTableLayout();
			this.innerLayout.setSizeUndefined();
			this.innerLayout.setCommitImmediately(false);

			// view setup
			this.innerLayout.setView(new ResultDBViewRoot(experiment));
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