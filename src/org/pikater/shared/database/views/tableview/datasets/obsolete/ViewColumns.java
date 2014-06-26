package org.pikater.shared.database.views.tableview.datasets.obsolete;

public enum ViewColumns {	
	META_DATA_TYPE("Type"),
	GLOBAL_META_NUMBER_OF_INSTANCES("Number of Instances"),
	GLOBAL_META_DEFAULT_TASK("Default Task"),
	ATTR_CAT_META_RATIO_OF_MISSING_VALUES("Ratio of Missing Values"),
	ATTR_CAT_META_NUMBER_OF_CATEGORIES("Number of Categories"),
	ATTR_NUM_META_AVERAGE("Average"),
	ATTR_NUM_META_CLASS_ENTROPY("Class Entropy"),
	ATTR_NUM_META_MAXIMUM("Maximum"),
	ATTR_NUM_META_MINIMUM("Minimum"),
	ATTR_NUM_META_MEDIAN("Median"),
	ATTR_NUM_META_MODE("Mode"),
	ATTR_NUM_META_RATIO_OF_MISSING_VALUES("Ratio of Missing Values"),
	ATTR_NUM_META_VARIANCE("Variance");
	
	public final String COLUMN_NAME;
    ViewColumns (String colName) {COLUMN_NAME = colName;}
	
}
