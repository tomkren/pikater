package org.pikater.web.vaadin.gui.server.components.dbviews.pickers;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.tableview.batches.experiments.ExperimentTableDBViewMin;
import org.pikater.web.vaadin.gui.server.components.dbviews.ExperimentDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.AbstractTablePicker;

public class ExperimentTablePicker extends AbstractTablePicker
{
	private static final long serialVersionUID = -3028057821490067892L;

	public ExperimentTablePicker(String caption, JPAUser owner, JPABatch batch)
	{
		super(caption);
		setView(new ExperimentDBViewRoot<ExperimentTableDBViewMin>(new ExperimentTableDBViewMin(owner, batch)));
	}
}