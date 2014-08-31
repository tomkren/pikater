package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.test;

import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.test.TestTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.TestDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.expandableview.ExpandableView;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.expandableview.ExpandableViewStep;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithDynamicSteps;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.DynamicNeighbourWizardStep;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;

public class TestExpandableView extends ExpandableView
{
	private static final long serialVersionUID = 5513288294724671701L;
	
	private final DBTableLayout mainTestLayout;
	
	public TestExpandableView()
	{
		super();
		setSizeUndefined();
		setWidth("100%");
		
		this.mainTestLayout = new DBTableLayout();
		this.mainTestLayout.setSizeFull();
		this.mainTestLayout.getTable().setMultiSelect(false); // this is required below
		this.mainTestLayout.setReadOnly(true);
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
	public void attach()
	{
		super.attach();
		enter(null);
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// always call these last, when you're absolutely ready to display the content
		this.mainTestLayout.setView(new TestDBViewRoot(new TestTableDBView()));
		super.finishInit(); // don't forget to!
	}

	@Override
	protected DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>> getFirstStep()
	{
		return new Step1(this);
	}
	
	//-----------------------------------------------------------
	// INDIVIDUAL STEPS
	
	private class Step1 extends ExpandableViewStep
	{
		public Step1(WizardWithDynamicSteps<IWizardCommon> parentWizard)
		{
			super(parentWizard, false);
			
			registerDBViewLayout(mainTestLayout);
		}

		@Override
		public String getCaption()
		{
			return "TestTableView1";
		}
		
		@Override
		public Component getContent()
		{
			return mainTestLayout;
		}
		
		@Override
		public ExpandableViewStep constructNextStep()
		{
			return constructNextStepFromView(null);
		}
		
		@Override
		protected ExpandableViewStep constructNextStepFromView(AbstractTableRowDBView view)
		{
			return new Step2(getParentWizard());
		}
	}
	
	private class Step2 extends ExpandableViewStep
	{
		public Step2(WizardWithDynamicSteps<IWizardCommon> parentWizard)
		{
			super(parentWizard, true);
			
			// registerDBViewLayout(mainTestLayout);
		}

		@Override
		public String getCaption()
		{
			return "TestTableView2";
		}

		@Override
		public Component getContent()
		{
			return mainTestLayout;
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