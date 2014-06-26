package org.pikater.shared.database.views.tableview.datasets.obsolete;

import java.util.ArrayList;
import java.util.List;

public class View {
	public List<String> getColumnTitles(List<ViewColumns> selectedColumns) {
		List<String> titles=new ArrayList<String>();
		for(ViewColumns col:selectedColumns){
			titles.add(col.COLUMN_NAME);
		}
		return titles;
	}
}
