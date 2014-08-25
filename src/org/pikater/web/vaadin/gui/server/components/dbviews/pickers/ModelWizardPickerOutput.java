package org.pikater.web.vaadin.gui.server.components.dbviews.pickers;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;

public class ModelWizardPickerOutput implements IWizardCommon
{
	private final JPAUser owner;
	private JPABatch batch;
	private JPAExperiment experiment;
	private JPAResult result;
	
	public ModelWizardPickerOutput(JPAUser owner)
	{
		this.owner = owner;
		this.batch = null;
		this.experiment = null;
		this.result = null;
	}
	
	public JPAUser getOwner()
	{
		return owner;
	}

	public JPABatch getBatch()
	{
		return batch;
	}

	public void setBatch(JPABatch batch)
	{
		this.batch = batch;
	}

	public JPAExperiment getExperiment()
	{
		return experiment;
	}

	public void setExperiment(JPAExperiment experiment)
	{
		this.experiment = experiment;
	}

	public JPAResult getResult()
	{
		return result;
	}

	public void setResult(JPAResult result)
	{
		this.result = result;
	}
}