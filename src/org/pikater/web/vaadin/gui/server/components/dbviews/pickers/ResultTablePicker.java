package org.pikater.web.vaadin.gui.server.components.dbviews.pickers;

import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.tableview.batches.experiments.results.ResultTableDBViewMin;
import org.pikater.web.vaadin.gui.server.components.dbviews.ResultDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.AbstractTablePicker;

public class ResultTablePicker extends AbstractTablePicker
{
	private static final long serialVersionUID = 3924832184060071896L;

	public ResultTablePicker(String caption, JPAUser owner, JPAExperiment experiment)
	{
		super(caption);
		setView(new ResultDBViewRoot<ResultTableDBViewMin>(new ResultTableDBViewMin(owner, experiment)));
	}
}