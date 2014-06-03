package org.pikater.shared.database.views.peter.models;

import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;


public class MetaDataRow {
	
	public static String nonDef="N/A"; 
	
	JPAGlobalMetaData global=null;
	JPAAttributeNumericalMetaData numerical=null;
	JPAAttributeCategoricalMetaData categorical=null;
	
	public MetaDataRow(JPAGlobalMetaData global) {
		super();
		this.global = global;
	}
	public MetaDataRow(JPAAttributeNumericalMetaData numerical) {
		super();
		this.numerical = numerical;
	}
	public MetaDataRow(JPAAttributeCategoricalMetaData categorical) {
		super();
		this.categorical = categorical;
	}
	
	public String getRatioOfMissingValues() {
		if(numerical!=null){
			return ""+numerical.getRatioOfMissingValues();
		}else if(categorical!=null){
			return ""+categorical.getRatioOfMissingValues();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String isTarget() {
		if(numerical!=null){
			return ""+numerical.isTarget();
		}else if(categorical!=null){
			return ""+categorical.isTarget();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String getNumberOfCategories() {
		if(categorical!=null){
			return ""+categorical.getNumberOfCategories();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String isReal() {
		if(numerical!=null){
			return ""+numerical.isReal();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String getMin() {
		if(numerical!=null){
			return ""+numerical.getMin();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String getMax() {
		if(numerical!=null){
			return ""+numerical.getMax();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String getMode() {
		if(numerical!=null){
			return ""+numerical.getMode();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String getMedian() {
		if(numerical!=null){
			return ""+numerical.getMedian();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String getClassEntropy() {
		if(numerical!=null){
			return ""+numerical.getClassEntropy();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String getVariance() {
		if(numerical!=null){
			return ""+numerical.getVariance();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String getAvarage() {
		if(numerical!=null){
			return ""+numerical.getAvarage();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String getDefaultTaskType() {
		if(global!=null){
			return global.getDefaultTaskType().getName();
		}else{
			return MetaDataRow.nonDef;
		}
	}
	public String getNumberofInstances() {
		if(global!=null){
			return ""+global.getNumberofInstances();
		}else{
			return MetaDataRow.nonDef;
		}
	}
}
