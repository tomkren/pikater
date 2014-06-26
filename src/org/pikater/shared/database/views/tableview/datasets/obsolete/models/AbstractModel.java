package org.pikater.shared.database.views.tableview.datasets.obsolete.models;

import java.util.List;

import org.pikater.shared.database.views.tableview.datasets.obsolete.ViewColumns;

public abstract class AbstractModel {
	protected String delim=" ";
	protected String finDelim=" EOL";
	
	protected String nonDef="N/A";
	
	public abstract String formattedString();
	public abstract List<String> columnFormat(List<ViewColumns> selectedColumns);
	
}
