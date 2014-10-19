/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.searchitems.IntervalSearchItem;

/**
 * Interface for input normalizers. These are used to normalize the inputs before model creation, 
 * training, evaluation etc.
 *
 * @author Martin Pilat
 */
public abstract class ModelInputNormalizer {
    
    /**
     * Method to normalize float values
     * @param dbl The float value to normalize
     * @param schema The range of possible values
     * @return The normalized value
     */
    public abstract double normalizeFloat(IValueData dbl, IntervalSearchItem schema);
    
    /**
     * Method to normalize integer values
     * @param n The integer value to normalize
     * @param schema The range of possible values
     * @return The normalized value
     */
    public abstract double normalizeInt(IValueData n, IntervalSearchItem schema);
    
}
