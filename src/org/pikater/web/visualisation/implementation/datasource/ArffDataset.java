package org.pikater.web.visualisation.implementation.datasource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.pikater.shared.database.exceptions.AttributeException;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.pglargeobject.PostgreLobAccess;
import org.pikater.web.visualisation.implementation.charts.axis.Axis;
import org.pikater.web.visualisation.implementation.charts.axis.CategoricalAxis;
import org.pikater.web.visualisation.implementation.charts.axis.ValueAxis;
import org.pikater.web.visualisation.implementation.charts.coloring.CategoricalColorer;
import org.pikater.web.visualisation.implementation.charts.coloring.Colorer;
import org.pikater.web.visualisation.implementation.charts.coloring.LinearColorer;
import org.pikater.web.visualisation.implementation.datasource.exception.DatasetNotInitializedException;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

public abstract class ArffDataset {

	private InputStream arffStream;
	private static int READER_CAPACITY=100;
	
	protected ArffDataset(InputStream stream){
		this.arffStream=stream;
		init();
	}
	
	private JPADataSetLO dslo=null;
	
	protected ArffDataset(JPADataSetLO source){
		this.dslo=source;
		initFromJPA();
	}
	
	protected ArffDataset(JPADataSetLO dslo,File cacheFile){
		this.dslo=dslo;
		try {
			this.arffStream=new FileInputStream(cacheFile);
		} catch (FileNotFoundException e) {
			this.arffStream=null;
		}
		init();
	}

	private void initFromJPA() {
		try {
			this.arffStream = PostgreLobAccess.getPostgreLargeObjectReader(dslo.getOID()).getInputStream();
			init();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	ArffReader arffReader=null;
	Instances data=null;
	protected boolean datasetInitialized=false;
	protected Instance currentInstance=null;

	private void init(){
		try{
			arffReader=new ArffReader(
					new BufferedReader(
							new InputStreamReader(this.arffStream)
							),
							READER_CAPACITY);
			data=arffReader.getStructure();
			//data.setClassIndex(data.numAttributes()-1);
			datasetInitialized=true;
		}catch(IOException ioe){
			datasetInitialized=false;
		}
	}
	
	public Instances getInstances(){
		return this.data;
	}

	public int translateAttributeNameToIndex(String attrName){
		if(data!=null){
			return data.attribute(attrName).index();
		}else{
			return -1;
		}
	}
	
	public String translateAttributeIndexToName(int index){
		if(data!=null){
			return data.attribute(index).name();
		}else{
			return null;
		}
	}

	public double getAttributeValue(int attributeIndex){
		return this.currentInstance.value(attributeIndex);
	}
	
	public int getNumberOfAttributes(){
		if(data!=null){
			return data.numAttributes();
		}else{
			throw new DatasetNotInitializedException();
		}
	}
	
	public boolean next() throws IOException{
		currentInstance=null;
		if(datasetInitialized){
			currentInstance=arffReader.readInstance(data);
			if(currentInstance!=null){
				return true;
			}
		}else{
			throw new DatasetNotInitializedException();
		}
		return false;
	}
	
	public Axis getAxis(int attrIndex){
		Attribute attribute=data.attribute(attrIndex);
		Axis res;
		if(attribute.isNominal()){
			String[] values=new String[attribute.numValues()];
			for(int i=0;i<values.length;i++){
				values[i]=attribute.value(i);
			}
			res = new CategoricalAxis(values);
		}else{
			if(dslo!=null){
				try {
					res=new ValueAxis(
							dslo.getAttributeMinValue(attribute.name()),
							dslo.getAttributeMaxValue(attribute.name()));
				} catch (AttributeException e) {
					//however, this shouldn't happen, because stream contains 
					e.printStackTrace();
					res=new ValueAxis();
				}
			}else{
				res = new ValueAxis();
			}
		}
		res.setCaption(attribute.name());
		return res;
	}
	
	public Colorer getColorer(int attrIndex){
		Attribute attribute=data.attribute(attrIndex);
		if(attribute.isNominal()){
			return new CategoricalColorer(attribute.numValues());
		}else{
			if(dslo!=null){
				try {
					return new LinearColorer(
							dslo.getAttributeMinValue(attribute.name()),
							dslo.getAttributeMaxValue(attribute.name())
							);
				} catch (AttributeException e) {
					//this shouldn't happen, because stored dataset don't change
					e.printStackTrace();
					return new LinearColorer();
				}
			}else{
				return new LinearColorer();
			}
		}	
	}
	
	public int getNumberOfInstances(){
		if(dslo!=null){
			return dslo.getGlobalMetaData().getNumberofInstances();
		}else{
			return -1;
		}
	}
	
	public void close() throws IOException{
		arffStream.close();
	}
}
