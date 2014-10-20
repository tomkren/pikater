package org.pikater.core.agents.system.metadata.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.pikater.core.agents.system.metadata.MetadataReader;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.logging.core.ConsoleLogger;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
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
			globMD.setDefaultTaskType(DAOs.TASKTYPEDAO.createOrGetByName(md.getDefaultTask()));
			globMD.setAttributeType(md.getAttributeType());
			globMD.setLinearRegressionDuration(md.getLinearRegressionDuration());
			return globMD;
		}
		
		public List<JPAAttributeMetaData> getJPAAttributeMetaData(){
			
			List<JPAAttributeMetaData> attrs=new LinkedList<JPAAttributeMetaData>();
			
			for (int i=md.getAttributeMetadataList().size()-1;i>=0;i--)
	        {
	            AttributeMetadata att= (AttributeMetadata)md.getAttributeMetadataList().get(i);
	            String attName=att.getName();
	            ConsoleLogger.log(Level.INFO, "MetadataQueen sent attribute with name: "+attName);
	            if(attName==null){
	            	attName="attr_"+i;
	            }
	            JPAAttributeMetaData jpaam=new JPAAttributeMetaData();
	            
	            if(att instanceof CategoricalAttributeMetadata){
	            	int numberOfCategories=((CategoricalAttributeMetadata)att).getNumberOfCategories();
	            	jpaam=new JPAAttributeCategoricalMetaData();
	            	((JPAAttributeCategoricalMetaData)jpaam).setNumberOfCategories(numberOfCategories);
	            	
	            }else if(att instanceof RealAttributeMetadata){
	            	RealAttributeMetadata ram = (RealAttributeMetadata) att;
	            	
	            	jpaam=new JPAAttributeNumericalMetaData();
	            	((JPAAttributeNumericalMetaData)jpaam).setAvarage(ram.getAvg());
	            	((JPAAttributeNumericalMetaData)jpaam).setMax(ram.getMax());
	            	((JPAAttributeNumericalMetaData)jpaam).setMedian(ram.getMedian());
	            	((JPAAttributeNumericalMetaData)jpaam).setMin(ram.getMin());
	            	///IMPORTANT!!!!   Getting mode value is not implemented yet!!!
	            	((JPAAttributeNumericalMetaData)jpaam).setMode(Double.NaN);
	            	((JPAAttributeNumericalMetaData)jpaam).setReal(true);
	            	((JPAAttributeNumericalMetaData)jpaam).setVariance(ram.getStandardDeviation());
	            	
	            }else if(att instanceof IntegerAttributeMetadata){
	            	IntegerAttributeMetadata ram = (IntegerAttributeMetadata) att;
	            	
	            	jpaam=new JPAAttributeNumericalMetaData();
	            	((JPAAttributeNumericalMetaData)jpaam).setAvarage(ram.getAvg());
	            	((JPAAttributeNumericalMetaData)jpaam).setMax(ram.getMax());
	            	((JPAAttributeNumericalMetaData)jpaam).setMedian(ram.getMedian());
	            	((JPAAttributeNumericalMetaData)jpaam).setMin(ram.getMin());
	            	///IMPORTANT!!!!   Getting mode value is not implemented yet!!!
	            	((JPAAttributeNumericalMetaData)jpaam).setMode(Double.NaN);
	            	((JPAAttributeNumericalMetaData)jpaam).setReal(false);
	            	((JPAAttributeNumericalMetaData)jpaam).setVariance(ram.getStandardDeviation());
	            }else{
	            	ConsoleLogger.log(Level.INFO, att.toString());
	            }
	            
	            jpaam.setRatioOfMissingValues(att.getRatioOfMissingValues());
            	jpaam.setTarget(att.isIsTarget());
            	jpaam.setName(attName);
	            jpaam.setClassEntropy(att.getAttributeClassEntropy());
	            jpaam.setEntropy(att.getEntropy());
	            jpaam.setOrder(att.getOrder());
	            
	            attrs.add(jpaam);
	        }
			
			return attrs;
		}
		
	}
