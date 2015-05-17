/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.agents.system.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.math3.stat.inference.GTest;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.OneWayAnova;
import org.pikater.core.ontology.subtrees.attribute.Attribute;
import org.pikater.core.ontology.subtrees.attribute.Instance;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.attributes.*;

/**
 * Class that can read metadata from the list of instances and attributes
 * @author Kuba
 */
public class MetadataReader {
    private double defValue = 0;
    
    /**
     * Constructor
     */
	public Metadata computeMetadata(DataInstances data) {
		
		Metadata metadata = new Metadata();		
							
        // number of instances
		metadata.setNumberOfInstances(data.getInstances().size());
		
		// number of attributes
		// we do not count targer attribute
		metadata.setNumberOfAttributes(data.getAttributes().size());

		// missing values
		boolean missing = false; 
		for (Instance instanceI : data.getInstances()) {
			
			if (!instanceI.getMissing().isEmpty()) {
				missing = true;
			}			
		}		
		metadata.setMissingValues(missing);
		
		// data type
		String type = "";
		for (Attribute attributeI : data.getAttributes()) {
			if (type.isEmpty()) {
				type = attributeI.getType();
			}
			if (!attributeI.getType().equals(type)) {
				type = "Multivariate";
			}
		}		
		metadata.setAttributeType(type);
		
		// default task 
        setTaskType(data, metadata);

        //Attributes
        readAttributesMetadata(data,metadata);  
        
        //int c=metadata.getNumberOfCategorical();
        //int r=metadata.getNumberOfReal();
        //int i=metadata.getNumberOfInteger();
        return metadata;
    }
    
    private void setTaskType(DataInstances data, Metadata metadata) {
    	
    	int index;
    	if (data.getClassIndex() >= 0) {
    		index = data.getClassIndex();
    	} else {
    		index = data.getAttributes().size() - 1;
    	}
    	
        if (data.getAttributes().get(index).getType().equals("Numeric")) {
            metadata.setDefaultTask("Regression");
        } else {
            metadata.setDefaultTask("Classification");
        }
    }
    
    private void readAttributesMetadata(DataInstances data,
    		Metadata metadata) {
    	
    	List<AttributeMetadata> attributeList =
    			metadata.getAttributeMetadataList();
    	
        for (int i = 0; i < metadata.getNumberOfAttributes(); i++) {
            AttributeMetadata attMet = readAttributeMetadata(data, i);
            attributeList.add(attMet);
        }
    }
    
    private AttributeMetadata readAttributeMetadata(DataInstances data,
    		int attributeNumber) {
    	
        AttributeType type = getAttributeType(data, attributeNumber);
        AttributeMetadata attributeMetadata = getAttributeMetadataInstance(type);
        
        
        setBaseAttributeProperties(data, attributeMetadata, attributeNumber);
        
        if (type!=AttributeType.Categorical) {
        	setNumericalAttributeProperties(data, attributeMetadata,
        			attributeNumber);
        } else {
        	setCategoricalAttributeProperties(data, attributeMetadata,
        			attributeNumber);
        }
        if (type != AttributeType.Real) {
        	countEntropies(data, attributeMetadata, attributeNumber);
        }
        return attributeMetadata;
    }
    
    private void setBaseAttributeProperties(DataInstances data,
    		AttributeMetadata metadata, int attributeNumber) {
    	
        Attribute actualAttribute = (Attribute)data.getAttributes().get(attributeNumber);        
        
        metadata.setKolmogorovSmirnov(computeKolmogorovSmirnovPValue(data, attributeNumber));
        metadata.setMannWhitneyU(computeMannWhitneyUTestPValue(data, attributeNumber));
        metadata.setCovariance(computeCovariance(data, attributeNumber));
        
        metadata.setOrder(attributeNumber);  
        metadata.setName(actualAttribute.getName());
        
        if (attributeNumber == data.getClassIndex()) {
        	metadata.setIsTarget(true);
        }
        setRatioMissingValues(data, attributeNumber, metadata);
    }
    
    private double[] getValuesArray(DataInstances data, int attributeNumber, int missingAttrIndex){
    	List<Double> values = new ArrayList<Double>();    
    	
    	for (Instance instanceI : data.getInstances()) {
    		List<Boolean> missingList = instanceI.getMissing();
                if ((boolean)missingList.get(missingAttrIndex)) {
                    continue;
                }
                values.add((Double)instanceI.getValues().get(attributeNumber));
            }
    	
    	return listToArray(values);
    }
    
    private double computeCovariance(DataInstances data, int attributeNumber){
    	Covariance cov = new Covariance();
    	
    	double[] valuesArray = getValuesArray(data, attributeNumber, attributeNumber);
		double[] classValuesArray = getValuesArray(data, data.getClassIndex(), attributeNumber);
		
		try{
			double val = cov.covariance(valuesArray, classValuesArray);
			if(Double.isNaN(val)){
				return 0.0;
			}else{
				return val;
			}
			
		}catch(MathIllegalArgumentException e){
			
			return 0.0;
		}
    }
    
    private double computeKolmogorovSmirnovPValue(DataInstances data, int attributeNumber){
    	
        KolmogorovSmirnovTest kst = new KolmogorovSmirnovTest();
		
		double[] valuesArray = getValuesArray(data, attributeNumber, attributeNumber);
		double[] classValuesArray = getValuesArray(data, data.getClassIndex(), attributeNumber);
		
		try{
			
			double val = kst.kolmogorovSmirnovTest(valuesArray, classValuesArray);
			if(Double.isNaN(val)){
				return 0.0;
			}else{
				return val;
			}
			
		}catch(TooManyIterationsException | InsufficientDataException | NullArgumentException e){
			
			return 0.0;
		}
    }
    
    private double computeMannWhitneyUTestPValue(DataInstances data, int attributeNumber){
    	
        MannWhitneyUTest utest = new MannWhitneyUTest();
		
		double[] valuesArray = getValuesArray(data, attributeNumber, attributeNumber);
		double[] classValuesArray = getValuesArray(data, data.getClassIndex(), attributeNumber);
		
		try{
			
			double val = utest.mannWhitneyU(valuesArray, classValuesArray);
			if(Double.isNaN(val)){
				return 0.0;
			}else{
				return val;
			}
			
		}catch(NoDataException | NullArgumentException e){
			
			return 0.0;
		}
    }
    
    private double computeAnovaValue(DataInstances data, int attributeNumber){
    	int classIdx = data.getClassIndex();
    	
    	if(attributeNumber == classIdx)
    		return 0.0;
    	
    	HashMap<Object, ArrayList<Double>> valuesByCategory = new HashMap<>();

        for (int i = 0; i < data.getInstances().size(); i++) {
            Instance inst = data.getInstances().get(i);
            Object key = inst.getValues().get(attributeNumber);

            if (!valuesByCategory.containsKey(key)) {
                valuesByCategory.put(key, new ArrayList<Double>());
            }

            valuesByCategory.get(key).add(inst.getValues().get(classIdx));
        }

        ArrayList<double[]> values = new ArrayList<>();

        for (Object key : valuesByCategory.keySet()) {
            values.add(listToArray(valuesByCategory.get(key)));
        }

        OneWayAnova anova = new OneWayAnova();
 
        try {
            double val = anova.anovaPValue(values);
            if(Double.isNaN(val)){
				return 0.0;
			}else{
				return val;
			}
        } catch (
        		DimensionMismatchException | 
        		NullArgumentException | 
        		ConvergenceException | 
        		MaxCountExceededException e) {
        	
            return 0.0;//TODO: anova selhala, nevim, kolik nastavit... rozhodli jsme pro nulu
        }
    }
    
    static double[] listToArray(List<Double> list) {

        double[] arr = new double[list.size()];

        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }

        return arr;
    }
    
    /**
     * Counts entropy for the {@link DataInstances}
     */
    private void countEntropies(DataInstances data,
    		AttributeMetadata metadata, int attributeNumber){
    	
        List<Object> values = new ArrayList<Object>();     
        List<Object> classValues = new ArrayList<Object>();        

		for (Instance instanceI : data.getInstances()) {
		List<Boolean> missingList = instanceI.getMissing();
		
            if ((boolean)missingList.get(attributeNumber)) {
                continue;
            }
            values.add(instanceI.getValues().get(attributeNumber));
            classValues.add(instanceI.getValues().get(data.getClassIndex()));
        }
        double entropy = Entropy.countEntropy(values);
        metadata.setEntropy(entropy);        
        double classEntropy =
        		Entropy.countEntropyClassAttribute(values, classValues);
        metadata.setAttributeClassEntropy(classEntropy);
    }
    
    @SuppressWarnings("unchecked")
	private <TReturn> List<TReturn> getAttributeData(
    		DataInstances data,
    		int attributeNumber)
    		{
    	List<TReturn> values=new ArrayList<TReturn>();       
        
		for (Instance instanceI : data.getInstances()) {
		List<Boolean> missingList = instanceI.getMissing();
            if ((boolean)missingList.get(attributeNumber)) {
                continue;
            }
            values.add((TReturn)instanceI.getValues().get(attributeNumber));
        }
		
		return values;
    }
    
    private double[] getAverageBins(int binCount, int overallItemCount){
    	double[] values = new double[binCount];
        for(int i=0;i<values.length;i++){
        	values[i] = overallItemCount / (double)binCount;
        }
        return values;
    }
    
    private double[] getNormalBins(int overalItemCount){
    	double[] values= new double[]
    			{
    			0.001*overalItemCount,
    			0.005*overalItemCount,
    			0.017*overalItemCount,
    			0.044*overalItemCount,
    			0.092*overalItemCount,
    			0.150*overalItemCount,
    			0.191*overalItemCount,
    			0.191*overalItemCount,
    			0.150*overalItemCount,
    			0.092*overalItemCount,
    			0.044*overalItemCount,
    			0.017*overalItemCount,
    			0.005*overalItemCount,
    			0.001*overalItemCount
    			};
    	return values;
    }
    
    private long[] getHistogram(double[] items, double mean, double estimatedStandardDeviation){
    	long[] histogram = new long[14];
    	
    	double[] barriers=new double[]{
    			mean - 3 * estimatedStandardDeviation,
    			mean - 2.5 * estimatedStandardDeviation,
    			mean - 2 * estimatedStandardDeviation,
    			mean - 1.5 * estimatedStandardDeviation,
    			mean - 1 * estimatedStandardDeviation,
    			mean - 0.5 * estimatedStandardDeviation,
    			mean ,
    			mean + 0.5 * estimatedStandardDeviation,
    			mean + estimatedStandardDeviation,
    			mean + 1.5 * estimatedStandardDeviation,
    			mean + 2 * estimatedStandardDeviation,
    			mean + 2.5 * estimatedStandardDeviation,
    			mean + 3 * estimatedStandardDeviation,
    	};
    	
    	for(int i=0;i<items.length;i++){
    		double item = items[i];
    		
    		//we check item, to which interval it belongs
    		for(int j=0;j<barriers.length; j++) {
    			double barJ =barriers[j];    		
    	   		
    			if(item < barJ){
        			histogram[j]++;
        			break;
        		}
    		}
    		//the last subinterval is checked for >=
    		if(item >= barriers[12]){
    			histogram[13]++;
    		}
    		
    	}
    	
    	return histogram;
    	
    }
    
    private void setCategoricalAttributeProperties(DataInstances data,
    		AttributeMetadata metadata, int attributeNumber) {
    	
        Attribute att = (Attribute)data.getAttributes().get(attributeNumber);
        CategoricalAttributeMetadata met = (CategoricalAttributeMetadata)metadata;
        
        int categoryCount = att.getValues().size();
        
        met.setNumberOfCategories(categoryCount);
        
        
        List<Double> values=getAttributeData(data, attributeNumber);
        
        
        Map<Integer,Integer> groupMap =new HashMap<Integer,Integer>();
        
        for(Double value : values){
        	
        	int key = value.intValue();
        	
        	if(groupMap.containsKey(key)){
        		int count = groupMap.get(key);
        		groupMap.put(key, count+1);
        	}else{
        		groupMap.put(key, 1);
        	}
        }
        
        Object[] observeda = groupMap.values().toArray();
        
        
        long[] observed =  new long[observeda.length];
        for(int i=0;i<observed.length;i++){
        	observed[i] = ((Integer)observeda[i]).longValue();
        }
        
        double[] expected = getAverageBins(categoryCount,values.size());
        
        double chiSquareValue = computeChiSquareValue(expected, observed);
        if (Double.isNaN(chiSquareValue)) {
        	chiSquareValue = 0;
        }
        met.setChiSquare(chiSquareValue);
        
        double chiSquareTestValue = computeChiSquareTestValue(expected, observed);
        if (Double.isNaN(chiSquareTestValue)) {
        	chiSquareTestValue = 0;
        }
        met.setChiSquareTest(chiSquareTestValue);
        
        double gTestValue = computeGTestValue(expected, observed);
        if (Double.isNaN(gTestValue)) {
        	gTestValue = 0;
        }
        met.setGTest(gTestValue);
        
        double anovaPValue = computeAnovaValue(data, attributeNumber);
        if (Double.isNaN(anovaPValue)) {
        	anovaPValue = 0;
        }
        met.setAnovaPValue(anovaPValue);
        
    }
    
    private double computeChiSquareValue(double[] expected, long[] observed){
    	ChiSquareTest cst = new ChiSquareTest();
        try{
        	double chiSquareValue = cst.chiSquare(expected, observed);
            if (Double.isNaN(chiSquareValue)) {
            	chiSquareValue = 0;
            }
        	return chiSquareValue;
        }catch(
        		NotPositiveException|
        		NotStrictlyPositiveException|
        		DimensionMismatchException ex){
        	return Double.NaN;
        }
    }
    
    private double computeChiSquareTestValue(double[] expected, long[] observed){
    	ChiSquareTest cst = new ChiSquareTest();
        try{
        	double chiSquareTestValue = cst.chiSquareTest(expected, observed);
            if (Double.isNaN(chiSquareTestValue)) {
            	chiSquareTestValue = 0;
            }
        	return chiSquareTestValue;
        }catch(
        		NotPositiveException|
        		NotStrictlyPositiveException|
        		DimensionMismatchException ex){
        	return Double.NaN;
        }
    }
    
    private double computeGTestValue(double[] expected, long[] observed){
    	GTest gt = new GTest();
        
        try{
        	double gTestValue = gt.gTest(expected, observed);
            if (Double.isNaN(gTestValue)) {
            	gTestValue = 0;
            }
        	return gTestValue;
        }catch(
        		NotPositiveException|
        		NotStrictlyPositiveException|
        		DimensionMismatchException|
        		MaxCountExceededException ex)
        {
        	return Double.NaN;
        }
    }
    
    private double getKurtosisValue(double[] items){
    	Kurtosis kurtosis=new Kurtosis();
    	return kurtosis.evaluate(items,0,items.length);
    }
    
    private double getSkewnessValue(double[] items){
    	Skewness skewness = new Skewness();
    	return skewness.evaluate(items, 0, items.length);
    }
    
    private void setNumericalAttributeProperties(DataInstances data,
    		AttributeMetadata metadata, int attributeNumber) {
        List<Double> values = getAttributeData(data, attributeNumber);      
        NumericalAttributeMetadata met=(NumericalAttributeMetadata)metadata;
		
        Collections.sort(values);
        if (!values.isEmpty()) {
        	met.setMin(values.get(0));
        } else {
        	met.setMin(defValue);
        }
        
        if (!values.isEmpty()) {
        	met.setMax(values.get(values.size()-1));
        } else {
        	met.setMax(defValue);
        }
        double average = 0;
        double squareaverage = 0;
        double n = values.size();
        
        for (int i=0;i<values.size();i++) {
            double currentvalue=values.get(i);
            average+=(currentvalue/n);
            squareaverage+=((currentvalue*currentvalue)/n);
        }
        double variation=squareaverage-(average*average);
        met.setAvg(average);
        
        double stdDeviation = Math.sqrt(variation);
        met.setStandardDeviation(stdDeviation);
        met.setVariation(variation);
        
        double mean = average;
        met.setMean(mean);
        
        
        double q1 = 0;
        double q2 = 0;
        double q3 = 0;
        
        if (values.size() > 0) {
	        int iq1 = values.size()/4;//index of first quartil
	        q1 = values.get(iq1);
	        met.setQ1(q1);
	        int iq2 = values.size()/2;//index of second quartil
	        q2 = values.get(iq2);
	        met.setQ2(q2);
	        int iq3 = 3*values.size()/4;//index of third quartil
	        q3 = values.get(iq3);
	        met.setQ3(q3);
        }
        
        int half=(int)Math.floor(values.size()/2.0);
        
        if (!values.isEmpty()) {
        	met.setMedian(values.get(half));
        } else {
        	met.setMedian(defValue);
        }
        
        double[] items = new double[values.size()];
        for(int i = 0; i < items.length; i++){
        	items[i] = (double)values.get(i);
        }
        
        long[] observed = getHistogram(items, mean, stdDeviation);
        
        double[] expected = getNormalBins(items.length);
        
        double chiSquareNormalD = computeChiSquareValue(expected, observed);
        if (Double.isNaN(chiSquareNormalD)) {
        	chiSquareNormalD = 0;
        }
        met.setChiSquareNormalD(chiSquareNormalD);
        
        double chiSquareTestNormalD = computeChiSquareTestValue(expected, observed);
        if (Double.isNaN(chiSquareTestNormalD)) {
        	chiSquareTestNormalD = 0;
        }
        met.setChiSquareTestNormalD(chiSquareTestNormalD);
        
        double gTestnormalD = computeGTestValue(expected, observed);
        if (Double.isNaN(gTestnormalD)) {
        	gTestnormalD = 0;
        }
        met.setgTestnormalD(gTestnormalD);
        
        double skewness = getSkewnessValue(items);
        if (Double.isNaN(skewness)) {
        	skewness = 0;
        }
        met.setSkewness(skewness);
        
        double kurtosis = getKurtosisValue(items);
        if (Double.isNaN(kurtosis)) {
        	kurtosis = 0;
        }
        met.setKurtosis(kurtosis);
        
    }
    
    private void setRatioMissingValues(DataInstances data,
    		int attributeNumber, AttributeMetadata attributeMetadata) {
    	
        double numberOfValues = data.getInstances().size();
        double numberOfMissingValues=0;

		for (Instance instanceI : data.getInstances() ) {
			List<Boolean> missingList = instanceI.getMissing();

			if ((boolean)missingList.get(attributeNumber)) {
                numberOfMissingValues++;
            }
        }
		if (numberOfValues > 0) {
	      attributeMetadata.setRatioOfMissingValues(
	    		  numberOfMissingValues/numberOfValues);
	    }
    }  
    
    private AttributeMetadata getAttributeMetadataInstance(
    		AttributeType type) {
    	
        switch (type) {
            case Categorical: 
                return new CategoricalAttributeMetadata();
            case Integer: 
                return new IntegerAttributeMetadata();
            default: return new RealAttributeMetadata();
        }
    }
    
    private AttributeType getAttributeType(DataInstances data,
    		int attributeNumber) {
    	
    	Attribute attribute = (Attribute)
        		data.getAttributes().get(attributeNumber);
        
        if (attribute.getType().equals("NOMINAL")) {
            return AttributeType.Categorical;
        }
        boolean canBeInt = true;
		for (Instance instanceI : data.getInstances()) {
			List<Boolean> missingList = instanceI.getMissing();
			
            if ((boolean)missingList.get(attributeNumber)) {
                continue;
            } else {
            	List<Double> values = instanceI.getValues();
                Object value = values.get(attributeNumber);                            
                if (value instanceof Double) {
                    double doub=(double)value;
                    if (!((doub == Math.floor(doub)) &&
                    		!Double.isInfinite(doub))) {
                        canBeInt= false;
                    }
                } else {
                    return AttributeType.Categorical;
                }
            }
        }
		
		if (canBeInt) {
			return AttributeType.Integer;
		} else {
			return AttributeType.Real;
		}
		
    }
}
