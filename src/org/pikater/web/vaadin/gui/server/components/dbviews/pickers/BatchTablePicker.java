package org.pikater.web.vaadin.gui.server.components.dbviews.pickers;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.tableview.batches.BatchTableDBViewUserMin;
import org.pikater.web.vaadin.gui.server.components.dbviews.BatchDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.AbstractTablePicker;

public class BatchTablePicker extends AbstractTablePicker
{
	private static final long serialVersionUID = 3006012644804864247L;

	public BatchTablePicker(String caption, JPAUser owner)
	{
		super(caption);
		setView(new BatchDBViewRoot<BatchTableDBViewUserMin>(new BatchTableDBViewUserMin(owner)));
	}
}