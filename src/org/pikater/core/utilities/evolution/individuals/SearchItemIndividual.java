package org.pikater.core.utilities.evolution.individuals;

import org.pikater.core.ontology.subtrees.newoption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newoption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.searchitems.IntervalSearchItem;
import org.pikater.core.ontology.subtrees.search.searchitems.SearchItem;
import org.pikater.core.ontology.subtrees.search.searchitems.SetSItem;
import org.pikater.core.utilities.evolution.RandomNumberGenerator;
import org.pikater.core.utilities.evolution.surrogate.ModelInputNormalizer;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.util.Arrays;

/**
 * Represents an individual as an array of SearchItem objects. Can be used to search the parameters
 * of machine-learning methods (computational agents).
 * 
 * Each individual has a schema, which describes the possible values on each position, and it also 
 * has the particular value for each position.
 *
 * @author Martin Pilat
 */
public class SearchItemIndividual extends MultiobjectiveIndividual {

    SearchItem[] schema;
    IValueData[] items;

    /**
     * Constructor. Initializes the individual with empty array of given length.
     * 
     * @param n the length of the array
     */
    
    public SearchItemIndividual(int n) {
        schema = new SearchItem[n];
        items = new IValueData[n];
    }
    
    /**
     * Sets the value of the {@link SearchItem} on the position {@code n} 
     * @param n the position to set
     * @param s the item to set on this position
     */
    
    public void setSchema(int n, SearchItem s) {
        schema[n] = s;
    }
    
    /**
     * Returns the schema on the {@code n}-th position.
     * 
     * @param n the position
     * @return the SearchItem on {@code n}-th position.
     */

    public SearchItem getSchema(int n) {
        return schema[n];
    }
    
    @Override
    public IValueData get(int n) {
        return items[n];
    }

    @Override
    public void set(int n, IValueData o) {
        items[n] = o;
    }

    @Override
    public int length() {
        return items.length;
    }

    @Override
    public void randomInitialization() {
        for (int i = 0; i < length(); i++) {
            items[i] = schema[i].randomValue(RandomNumberGenerator.getInstance().getRandom());
        }   
    }
    
    @Override
    public String toString() {
        return Arrays.toString(items);
    }
    
    @Override
    public SearchItemIndividual clone() {
        SearchItemIndividual newSI = (SearchItemIndividual) super.clone();
        
        newSI.schema = schema;
        newSI.items = new IValueData[items.length];
        
        for (int i = 0; i < items.length; i++) {
            newSI.items[i] = items[i].clone();
        }
        
        newSI.fitnessValue = fitnessValue;
        newSI.objectiveValue = objectiveValue;
        
        return newSI;
        
    }
    
    /**
     * Creates an empty dataset from the schema of the individual. Assigns the
     * attributes types according the schema specified in the individual. 
     * 
     * @return The empty dataset representing the schema of the individual.
     */
    
    public Instances emptyDatasetFromSchema() {        
        
        FastVector attributes = new FastVector();
        
        for (int i = 0; i < length(); i++) {
            if (schema[i] instanceof SetSItem) {
                FastVector values = new FastVector();
                
                for (IValueData valueI : schema[i].possibleValues() ) {
                    values.addElement(valueI);
                }
                attributes.addElement(new Attribute("a" + i, values));
                continue;
            }
            attributes.addElement(new Attribute("a" + i));
        }
        
        attributes.addElement(new Attribute("class"));
        
        Instances inst = new Instances("train", attributes, 0);
        inst.setClassIndex(attributes.size() - 1);
        
        return inst;
        
    }
    
    /**
     * Transforms the individual into a Weka instance which can be used for
     * surrogate model training.
     * 
     * The instance does not have any dataset assigned. Use the dataset returned
     * by {@link #emptyDatasetFromSchema() emptyDatasetFromSchema} method.
     * 
     * @return the instance representing this individual WITHOUT the class value
     * set.
     */
    public Instance toWekaInstance(ModelInputNormalizer norm) {

        Instance inst = new Instance(items.length + 1);
        inst.setDataset(emptyDatasetFromSchema());
        
        for (int i = 0; i < items.length; i++) {
            if (schema[i] instanceof SetSItem) {
                inst.setValue(i, items[i].toString());
            } else {
                IntervalSearchItem searchItem=(IntervalSearchItem)schema[i];
                if (searchItem.getMin() instanceof BooleanValue) {
                    inst.setValue(i, "False".equals(items[i].hackValue()) ? 0.0 : 1.0);
                } else if (searchItem.getMin() instanceof IntegerValue) {
                    inst.setValue(i, norm.normalizeInt(items[i], (IntervalSearchItem)schema[i]));
                } else if (searchItem.getMin() instanceof FloatValue) {
                    inst.setValue(i, norm.normalizeFloat(items[i], (IntervalSearchItem)schema[i]));
                }
            }
        }
        
        return inst;
    }
}