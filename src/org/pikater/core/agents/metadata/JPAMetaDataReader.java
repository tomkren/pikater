package org.pikater.core.agents.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.pikater.core.agents.system.metadata.MetadataReader;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.attributes.AttributeMetadata;
import org.pikater.core.ontology.subtrees.metadata.attributes.CategoricalAttributeMetadata;
import org.pikater.core.ontology.subtrees.metadata.attributes.IntegerAttributeMetadata;
import org.pikater.core.ontology.subtrees.metadata.attributes.RealAttributeMetadata;

import weka.core.Instances;

public class JPAMetaDataReader {

		MetadataReader reader;
		Metadata md=null;
		
		public JPAMetaDataReader(){
			reader=new MetadataReader();
		}
		
		public JPAMetaDataReader(Metadata metadata){
			this.md=metadata;
		}
		
		public void readFile(File file) throws FileNotFoundException, IOException{	
			DataInstances data=new DataInstances();
			data.fillWekaInstances(new Instances(new BufferedReader(new FileReader(file))));
			md=reader.computeMetadata(data);
		}
		
		public JPAGlobalMetaData getJPAGlobalMetaData(){
			JPAGlobalMetaData globMD=new JPAGlobalMetaData();
			globMD.setNumberofInstances(md.getNumberOfInstances());
			globMD.setDefaultTaskType(DAOs.taskTypeDAO.createOrGetByName(md.getDefaultTask()));
			return globMD;
		}
		
		public List<JPAAttributeMetaData> getJPAAttributeMetaData(){
			
			List<JPAAttributeMetaData> attrs=new LinkedList<JPAAttributeMetaData>();
			
			for (int i=md.getAttributeMetadataList().size()-1;i>=0;i--)
	        {
	            AttributeMetadata att= (AttributeMetadata)md.getAttributeMetadataList().get(i);
	            
	            if(att instanceof CategoricalAttributeMetadata){
	            	int numberOfCategories=((CategoricalAttributeMetadata)att).getNumberOfCategories();
	            	JPAAttributeCategoricalMetaData attrCat=new JPAAttributeCategoricalMetaData();
	            	attrCat.setNumberOfCategories(numberOfCategories);
	            	attrCat.setRatioOfMissingValues(att.getRatioOfMissingValues());
	            	attrCat.setTarget(att.isIsTarget());
	            	attrs.add(attrCat);
	            }else if(att instanceof RealAttributeMetadata){
	            	RealAttributeMetadata ram= ((RealAttributeMetadata)att);
	            	
	            	JPAAttributeNumericalMetaData numericalAttributeMetaData=new JPAAttributeNumericalMetaData();
	            	numericalAttributeMetaData.setAvarage(ram.getAvg());
	            	numericalAttributeMetaData.setClassEntropy(ram.getAttributeClassEntropy());
	            	numericalAttributeMetaData.setMax(ram.getMax());
	            	numericalAttributeMetaData.setMedian(ram.getMedian());
	            	numericalAttributeMetaData.setMin(ram.getMin());
	            	///IMPORTANT!!!!   Getting mode value is not implemented yet!!!
	            	numericalAttributeMetaData.setMode(Double.NaN);
	            	
	            	numericalAttributeMetaData.setReal(true);
	            	numericalAttributeMetaData.setVariance(ram.getStandardDeviation());
	            	numericalAttributeMetaData.setTarget(att.isIsTarget());
	            	numericalAttributeMetaData.setRatioOfMissingValues(att.getRatioOfMissingValues());
	            	attrs.add(numericalAttributeMetaData);
	            }else if(att instanceof IntegerAttributeMetadata){
	            	IntegerAttributeMetadata ram= ((IntegerAttributeMetadata)att);
	            	
	            	JPAAttributeNumericalMetaData numericalAttributeMetaData=new JPAAttributeNumericalMetaData();
	            	numericalAttributeMetaData.setAvarage(ram.getAvg());
	            	numericalAttributeMetaData.setClassEntropy(ram.getAttributeClassEntropy());
	            	numericalAttributeMetaData.setMax(ram.getMax());
	            	numericalAttributeMetaData.setMedian(ram.getMedian());
	            	numericalAttributeMetaData.setMin(ram.getMin());
	            	///IMPORTANT!!!!   Getting mode value is not implemented yet!!!
	            	numericalAttributeMetaData.setMode(Double.NaN);
	            	
	            	numericalAttributeMetaData.setReal(false);
	            	numericalAttributeMetaData.setVariance(ram.getStandardDeviation());
	            	
	            	numericalAttributeMetaData.setTarget(att.isIsTarget());
	            	numericalAttributeMetaData.setRatioOfMissingValues(att.getRatioOfMissingValues());
	            	attrs.add(numericalAttributeMetaData);
	            }else{
	            	System.out.println(att.toString());
	            }
	        }
			
			return attrs;
		}
		
	}
