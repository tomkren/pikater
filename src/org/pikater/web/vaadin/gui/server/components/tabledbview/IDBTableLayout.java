package org.pikater.web.vaadin.gui.server.components.tabledbview;

import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

public interface IDBTableLayout
{
	void dbViewActionCalled(ITableColumn column, AbstractTableRowDBView row, NamedActionDBViewValue originalAction);
}
