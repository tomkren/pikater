package org.pikater.shared.database.views.models;

import java.util.List;

import org.pikater.shared.database.views.ViewColumns;

public abstract class AbstractModel {
	protected String delim=" ";
	protected String finDelim=" EOL";
	
	protected String nonDef="N/A";
	
	public abstract String formattedString();
	public abstract List<String> columnFormat(List<ViewColumns> selectedColumns);
	
}
