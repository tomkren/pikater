package org.pikater.shared.database.views.models;


import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.views.MetaDataView;
import org.pikater.shared.database.views.ViewColumns;

import java.util.ArrayList;
import java.util.List;

public class MetaDataModel extends AbstractModel {
	
	private JPAAttributeNumericalMetaData attrNumMD=null;
	private JPAAttributeCategoricalMetaData attrCatMD=null;
	private JPAGlobalMetaData globMD=null;

	
	public MetaDataModel(JPAAttributeNumericalMetaData attributeNumericalMetaData){
		this.attrNumMD=attributeNumericalMetaData;
	}
	
	public MetaDataModel(JPAAttributeCategoricalMetaData attributeCategoricalMetaData){
		this.attrCatMD=attributeCategoricalMetaData;
	}
	
	public MetaDataModel(JPAGlobalMetaData globalMetaData){
		this.globMD=globalMetaData;
	}	
	
	@Override
	public String formattedString(){
		if(attrNumMD!=null){
			return  "Numerical: "+
					"Avg. = "+attrNumMD.getAvarage()+delim+
					"Cl.Ent. = "+attrNumMD.getClassEntropy()+delim+
					"Max. = "+attrNumMD.getMax()+delim+
					"Min. = "+attrNumMD.getMin()+delim+
					"Median = "+attrNumMD.getMedian()+delim+
					"Mode = "+attrNumMD.getMode()+delim+
					"RoMV = "+attrNumMD.getRatioOfMissingValues()+delim+
					"Variance = "+attrNumMD.getVariance()+finDelim
					;
		}else if(attrCatMD!=null){
			return "Categorical: "+
					"Cat.No. = "+attrCatMD.getNumberOfCategories()+delim+
					"RoMV = "+attrCatMD.getRatioOfMissingValues()+finDelim
					;
		}else if(globMD!=null){
			return "Global: "+
					"Inst.No. = "+globMD.getNumberofInstances()+delim+
					"Def.Task = "+globMD.getDefaultTaskType().getName()+finDelim
					;
		}else{
			return null;
		}
	}

	public List<String> getDefaultCategoricalMetaDataColumns(){
		return this.columnFormat(MetaDataView.DEFAULT_CATEGORICAL_COLUMNS);
	}
	
	public List<String> getDefaultNumericalMetaDataColumns(){
		return this.columnFormat(MetaDataView.DEFAULT_NUMERICAL_COLUMNS);
	}
	
	public List<String> getDefaultGlobalMetaDataColumns(){
		return this.columnFormat(MetaDataView.DEFAULT_GLOBAL_COLUMNS);
	}
	
	
	@Override
	public List<String> columnFormat(List<ViewColumns> selectedColumns) {
		List<String> row=new ArrayList<String>();
		for(ViewColumns col:selectedColumns){
			
			switch(col){
				case META_DATA_TYPE:
					if(globMD!=null)
						row.add(globMD.getEntityName());
					else if (attrCatMD!=null)
						row.add(attrCatMD.getEntityName());
					else if (attrNumMD!=null)
						row.add(attrNumMD.getEntityName());
					else
						row.add(nonDef);
					
					break;
				case GLOBAL_META_DEFAULT_TASK:
					if(globMD!=null)
						row.add(""+globMD.getDefaultTaskType().getName());
					else
						row.add(nonDef);
					break;
				case GLOBAL_META_NUMBER_OF_INSTANCES:
					if(globMD!=null)
						row.add(""+globMD.getNumberofInstances());
					else
						row.add(nonDef);
					break;
				case ATTR_CAT_META_NUMBER_OF_CATEGORIES:
					if(attrCatMD!=null)
						row.add(""+attrCatMD.getNumberOfCategories());
					else
						row.add(nonDef);
					break;
				case ATTR_CAT_META_RATIO_OF_MISSING_VALUES:
					if(attrCatMD!=null)
						row.add(""+attrCatMD.getRatioOfMissingValues());
					else
						row.add(nonDef);
					break;
				case ATTR_NUM_META_AVERAGE:
					if(attrNumMD!=null)
						row.add(""+attrNumMD.getAvarage());
					else
						row.add(nonDef);
					break;
				case ATTR_NUM_META_CLASS_ENTROPY:
					if(attrNumMD!=null)
						row.add(""+attrNumMD.getClassEntropy());
					else
						row.add(nonDef);
					break;
				case ATTR_NUM_META_MAXIMUM:
					if(attrNumMD!=null)
						row.add(""+attrNumMD.getMax());
					else
						row.add(nonDef);
					break;
				case ATTR_NUM_META_MINIMUM:
					if(attrNumMD!=null)
						row.add(""+attrNumMD.getMin());
					else
						row.add(nonDef);
					break;
				case ATTR_NUM_META_MEDIAN:
					if(attrNumMD!=null)
						row.add(""+attrNumMD.getMedian());
					else
						row.add(nonDef);
					break;
				case ATTR_NUM_META_MODE:
					if(attrNumMD!=null)
						row.add(""+attrNumMD.getMode());
					else
						row.add(nonDef);
					break;
				case ATTR_NUM_META_RATIO_OF_MISSING_VALUES:
					if(attrNumMD!=null)
						row.add(""+attrNumMD.getRatioOfMissingValues());
					else
						row.add(nonDef);
					break;
				case ATTR_NUM_META_VARIANCE:
					if(attrNumMD!=null)
						row.add(""+attrNumMD.getVariance());
					else
						row.add(nonDef);
					break;
				default:
					row.add("NON_DEF_ENUM");
			}

		}
		return row;
	}

}
