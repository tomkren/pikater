package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.datasets.compare;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBRow;
import org.pikater.shared.database.views.tableview.datasets.DatasetPickingTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.DatasetDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardForDialog;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.ParentAwareWizardStep;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DatasetCompareWizard extends WizardForDialog<DatasetCompareCommons>
{
	private static final long serialVersionUID = -2782484084003504941L;
	
	public DatasetCompareWizard(Window parentPopup, JPADataSetLO originalDataset)
	{
		super(parentPopup, new DatasetCompareCommons(originalDataset));
		
		addStep(new Step1(this));
		addStep(new Step2(this));
		addStep(new Step3(this));
		
		// TODO: test & integrate
	}
	
	//--------------------------------------------------------------
	// INDIVIDUAL STEPS
	
	private class Step1 extends ParentAwareWizardStep<DatasetCompareCommons, DatasetCompareWizard>
	{
		private final VerticalLayout vLayout;
		
		public Step1(DatasetCompareWizard parentWizard)
		{
			super(parentWizard);
			
			this.vLayout = new VerticalLayout();
			this.vLayout.setSizeFull();
			this.vLayout.setSpacing(true);
			
			Label label = new Label(String.format("First select attributes to be compared for '%s':", 
					getOutput().getDatasetOriginal().getFileName()));
			label.setSizeUndefined();
			label.setStyleName("v-label-undefWidth-wordWrap");
			
			this.vLayout.addComponent(label);
			this.vLayout.addComponent(getOutput().getFormOriginal());
			this.vLayout.setExpandRatio(getOutput().getFormOriginal(), 1);
		}

		@Override
		public String getCaption()
		{
			return "Select attributes...";
		}

		@Override
		public Component getContent()
		{
			return vLayout;
		}

		@Override
		public boolean onAdvance()
		{
			return true;
		}

		@Override
		public boolean onBack()
		{
			return true;
		}
	}
	
	private class Step2 extends ParentAwareWizardStep<DatasetCompareCommons, DatasetCompareWizard>
	{
		private final DBTableLayout innerLayout;
		
		public Step2(DatasetCompareWizard parentWizard)
		{
			super(parentWizard);
			
			this.innerLayout = new DBTableLayout();
			this.innerLayout.setSizeFull();
			this.innerLayout.setCaption("Select a row and click 'Next':");
			this.innerLayout.getTable().setMultiSelect(false);
			this.innerLayout.setView(new DatasetDBViewRoot<DatasetPickingTableDBView>(new DatasetPickingTableDBView()));
		}

		@Override
		public String getCaption()
		{
			return "Compare to...";
		}

		@Override
		public Component getContent()
		{
			return innerLayout;
		}

		@Override
		public boolean onAdvance()
		{
			AbstractTableRowDBView[] selectedViews = innerLayout.getTable().getViewsOfSelectedRows();
			if(selectedViews.length > 0)
			{
				// this assumes single select mode
				DataSetTableDBRow selectedView = (DataSetTableDBRow) selectedViews[0];
				getOutput().setCompareToDataset(selectedView.getDataset());
				return true;
			}
			else
			{
				MyNotifications.showError(null, "No table row (dataset) is selected.");
				return false;
			}
		}

		@Override
		public boolean onBack()
		{
			return false;
		}
	}
	
	private class Step3 extends ParentAwareWizardStep<DatasetCompareCommons, DatasetCompareWizard>
	{
		private final VerticalLayout vLayout;
		
		public Step3(DatasetCompareWizard parentWizard)
		{
			super(parentWizard);
			
			this.vLayout = new VerticalLayout();
			this.vLayout.setSizeFull();
			this.vLayout.setSpacing(true);
			
			Label label = new Label(String.format("And finally, select attributes to be compared for '%s':", 
					getOutput().getDatasetCompareTo().getFileName()));
			label.setSizeUndefined();
			label.setStyleName("v-label-undefWidth-wordWrap");
			
			this.vLayout.addComponent(label);
			this.vLayout.addComponent(getOutput().getFormCompareTo());
			this.vLayout.setExpandRatio(getOutput().getFormCompareTo(), 1);
		}

		@Override
		public String getCaption()
		{
			return "Select attributes...";
		}

		@Override
		public Component getContent()
		{
			return vLayout;
		}

		@Override
		public boolean onAdvance()
		{
			return false;
		}

		@Override
		public boolean onBack()
		{
			return true;
		}
	}
}