package org.pikater.web.vaadin.gui.server.components.dbviews.tableview;

import org.pikater.shared.database.views.base.QueryConstraints;

public interface IDBTableContainerContext
{
	QueryConstraints getQuery();
}