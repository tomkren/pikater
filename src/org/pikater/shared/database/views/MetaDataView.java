package org.pikater.shared.database.views;

import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.utils.ResultFormatter;
import org.pikater.shared.database.views.models.MetaDataModel;
import org.pikater.shared.database.views.models.MetaDataRow;

import java.util.ArrayList;
import java.util.List;

public class MetaDataView extends View {
	
	JPADataSetLO parentDSLO=null;
	
	public final static List<ViewColumns> DEFAULT_CATEGORICAL_COLUMNS=new ArrayList<ViewColumns>();
	public final static List<ViewColumns> DEFAULT_NUMERICAL_COLUMNS=new ArrayList<ViewColumns>();
	public final static List<ViewColumns> DEFAULT_GLOBAL_COLUMNS=new ArrayList<ViewColumns>();
	
	static{
		init();
	}
	
	public MetaDataView(JPADataSetLO parent){
		this.parentDSLO=parent;
	}
	
	public MetaDataView(int dataSetID) throws NoResultException{
		this.parentDSLO=DAOs.dataSetDAO.getByIDWithException(dataSetID);
	}
	
	private static void init(){
		initCategoricalCols();
		initNumericalCols();
		initGlobalCols();
	}
	
	private static void initCategoricalCols(){
		DEFAULT_CATEGORICAL_COLUMNS.add(ViewColumns.META_DATA_TYPE);
		DEFAULT_CATEGORICAL_COLUMNS.add(ViewColumns.ATTR_CAT_META_NUMBER_OF_CATEGORIES);
		DEFAULT_CATEGORICAL_COLUMNS.add(ViewColumns.ATTR_CAT_META_RATIO_OF_MISSING_VALUES);
	}
	
	private static void initNumericalCols(){
		DEFAULT_NUMERICAL_COLUMNS.add(ViewColumns.META_DATA_TYPE);
		DEFAULT_NUMERICAL_COLUMNS.add(ViewColumns.ATTR_NUM_META_AVERAGE);
		DEFAULT_NUMERICAL_COLUMNS.add(ViewColumns.ATTR_NUM_META_CLASS_ENTROPY);
		DEFAULT_NUMERICAL_COLUMNS.add(ViewColumns.ATTR_NUM_META_MAXIMUM);
		DEFAULT_NUMERICAL_COLUMNS.add(ViewColumns.ATTR_NUM_META_MEDIAN);
		DEFAULT_NUMERICAL_COLUMNS.add(ViewColumns.ATTR_NUM_META_MINIMUM);
		DEFAULT_NUMERICAL_COLUMNS.add(ViewColumns.ATTR_NUM_META_MODE);
		DEFAULT_NUMERICAL_COLUMNS.add(ViewColumns.ATTR_NUM_META_RATIO_OF_MISSING_VALUES);
		DEFAULT_NUMERICAL_COLUMNS.add(ViewColumns.ATTR_NUM_META_VARIANCE);
	}
	
	private static void initGlobalCols(){
		DEFAULT_GLOBAL_COLUMNS.add(ViewColumns.META_DATA_TYPE);
		DEFAULT_GLOBAL_COLUMNS.add(ViewColumns.GLOBAL_META_DEFAULT_TASK);
		DEFAULT_GLOBAL_COLUMNS.add(ViewColumns.GLOBAL_META_NUMBER_OF_INSTANCES);
	}
	
	
	public List<String> getDefaultCategoricalMetaDataTitles(){
		return this.getColumnTitles(DEFAULT_CATEGORICAL_COLUMNS);
	}
	
	public List<String> getDefaultNumericalMetaDataTitles(){
		return this.getColumnTitles(DEFAULT_NUMERICAL_COLUMNS);
	}
	
	public List<String> getDefaultGlobalMetaDataTitles(){
		return this.getColumnTitles(DEFAULT_GLOBAL_COLUMNS);
	}
	
	
	public MetaDataModel getGlobalMetaDataModel(){
		return new MetaDataModel(parentDSLO.getGlobalMetaData());
	}
	
	public List<MetaDataModel> getNumericalMetaDataModels(){
		List<MetaDataModel> models=new ArrayList<MetaDataModel>();
			
		for(JPAAttributeMetaData attribute:parentDSLO.getAttributeMetaData()){
			if(attribute instanceof JPAAttributeNumericalMetaData){
				models.add(new MetaDataModel((JPAAttributeNumericalMetaData)attribute));
			}
		}
			
		return models;
	}
	
	public List<MetaDataModel> getCategoricalMetaDataModels(){
		List<MetaDataModel> models=new ArrayList<MetaDataModel>();
		
		for(JPAAttributeMetaData attribute:parentDSLO.getAttributeMetaData()){
			if(attribute instanceof JPAAttributeCategoricalMetaData){
				models.add(new MetaDataModel((JPAAttributeCategoricalMetaData)attribute));
			}
		}
			
		return models;
	}
	
	public List<MetaDataModel> getMetaDataModels(){
		List<MetaDataModel> models=new ArrayList<MetaDataModel>();
		models.add(new MetaDataModel(parentDSLO.getGlobalMetaData()));
			
		for(JPAAttributeMetaData attribute:parentDSLO.getAttributeMetaData()){
			if(attribute instanceof JPAAttributeNumericalMetaData){
				models.add(new MetaDataModel((JPAAttributeNumericalMetaData)attribute));
			}else if(attribute instanceof JPAAttributeCategoricalMetaData){
				models.add(new MetaDataModel((JPAAttributeCategoricalMetaData)attribute));
			}
		}
			
		return models;
	}
	
	public List<MetaDataRow> getMetaDataRowModels(){
		List<MetaDataRow> models=new ArrayList<MetaDataRow>();
		
		models.add(
				new MetaDataRow(
						parentDSLO.getGlobalMetaData()
						)
				);
		
		for(JPAAttributeMetaData attribute:parentDSLO.getAttributeMetaData()){
			if(attribute instanceof JPAAttributeNumericalMetaData){
				models.add(new MetaDataRow((JPAAttributeNumericalMetaData)attribute));
			}else if(attribute instanceof JPAAttributeCategoricalMetaData){
				models.add(new MetaDataRow((JPAAttributeCategoricalMetaData)attribute));
			}
		}
		
		
		return models;
		
	}
	
}
