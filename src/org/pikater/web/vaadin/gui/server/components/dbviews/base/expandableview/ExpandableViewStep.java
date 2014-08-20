package org.pikater.web.vaadin.gui.server.components.dbviews.base.expandableview;

import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithDynamicSteps;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.DynamicNeighbourWizardStep;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;

public abstract class ExpandableViewStep extends DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>>
{
	public ExpandableViewStep(WizardWithDynamicSteps<IWizardCommon> parentWizard, boolean isLeaf)
	{
		super(parentWizard, isLeaf);
	}
	
	protected void registerDBViewLayout(final DBTableLayout layout)
	{
		if(!isLeaf())
		{
			layout.getTable().addValueChangeListener(new ValueChangeListener()
			{
				private static final long serialVersionUID = 2787932307187569389L;

				@Override
				public void valueChange(ValueChangeEvent event)
				{
					getParentWizard().getNextButton().setEnabled(!layout.getTable().getSelectedRowIDs().isEmpty());
				}
			});
			layout.getTable().addItemClickListener(new ItemClickEvent.ItemClickListener()
			{
				private static final long serialVersionUID = -243751947346194008L;

				@Override
				public void itemClick(ItemClickEvent event)
				{
					if(event.isDoubleClick())
					{
						/*
						 * The second click might deselect the row but we need it selected
						 * because of the "Next" button in wizards.
						 */
						
						layout.getTable().select(event.getItemId());
						getParentWizard().advance(constructNextStepFromView(
								layout.getTable().getContainerDataSource().getItem(event.getItemId()).getRowView()));
					}
				}
			});
		}
	}
	
	@Override
	public abstract ExpandableViewStep constructNextStep();
	protected abstract ExpandableViewStep constructNextStepFromView(AbstractTableRowDBView view);
}