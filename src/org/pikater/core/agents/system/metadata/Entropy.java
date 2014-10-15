/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.agents.system.metadata;

import java.util.*;

/**
 * Counts the entropy for the universal values
 * @author Kuba
 */
public class Entropy {
	
	/**
	 * Counts the entropy
	 */
    public static double countEntropy(List<Object> values) {
    	
        double n = values.size();
        double result = 0;
        Map<Object,Integer> hash =
        		new HashMap<Object,Integer>();
        
        for (Object valueI : values) {
            if (hash.containsKey(valueI)) {
                int current = hash.get(valueI);
                hash.put(valueI, current+1);
            } else {
                hash.put(valueI, 1);
            }            
        }
        for (Object keyI : hash.keySet()) {
            int count = hash.get(keyI);
            double pt = count/n;
            double logpt = Math.log(pt)/Math.log(2);
            result += pt*logpt;
        }
        result = result*-1;
        return result;
    }
    
    /**
     * Count entropy for the attribute values
     */
    public static double countEntropyClassAttribute(
    		List<Object> attributeValues, List<Object> classValues) {
    	
        double result = 0;
        HashSet<Object> targetValues = new HashSet<Object>();
        HashSet<Object> sourceValues = new HashSet<Object>();

        for (Object classValueI : classValues) {

            if (!targetValues.contains(classValueI)) {
                targetValues.add(classValueI);
            }            
        }
        for (Object attributeValueI : attributeValues) {
        	
            if (!sourceValues.contains(attributeValueI)) {
                sourceValues.add(attributeValueI);
            }            
        }
        //count H(A(v))
        
        double n = attributeValues.size();
        for (Object sourceValueI : sourceValues) {
        	
            double nav = getNumberOfInstancesWithSpecifiedAttributeValue(
            		attributeValues, sourceValueI);
            double hav = 0;
            for (Object target : targetValues) {
                double ntav =
                		getNumberOfInstancesWithSpecifiedAttributeClassValue(
                				attributeValues, classValues, sourceValueI,
                				target);
                double ratio=ntav/nav;
                if (ratio == 0) {
                	continue;
                }
                hav+=(ratio)*Math.log(ratio)/Math.log(2);
            }
            hav = hav*-1;         
            result += (nav*hav)/n;            
        }        
        return result;
    }
    
    private static double getNumberOfInstancesWithSpecifiedAttributeValue(
    		List<Object> attributeValues,Object fixedValue) {
        double result=0;
        for (Object attributeValueI : attributeValues) {
        	
            if (attributeValueI.equals(fixedValue)) {
            	result ++;
            }
        }
        return result;
    }
    
    private static double getNumberOfInstancesWithSpecifiedAttributeClassValue(
    		List<Object> attributeValues, List<Object> classValues,
    		Object fixedAttributeValue, Object fixedClassValue) {
    	
        double result = 0;
        for (int i = 0 ; i < attributeValues.size(); i++) {
            if (attributeValues.get(i).equals(fixedAttributeValue) &&
            		classValues.get(i).equals(fixedClassValue)) {
            	result++;
            }
        }
        return result;
    }
    
}
