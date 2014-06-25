package org.pikater.web.vaadin.gui.server.components.tabledbview;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.NamedActionDBViewValue;

public interface IDBTableLayout
{
	void dbViewActionCalled(IColumn column, AbstractTableRowDBView row, NamedActionDBViewValue originalAction);
}
