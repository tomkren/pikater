/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.searchItems.IntervalSearchItem;

/**
 *
 * @author Martin Pilat
 */
public abstract class ModelInputNormalizer {
    
    public abstract double normalizeFloat(IValueData dbl, IntervalSearchItem schema);
    public abstract double normalizeInt(IValueData n, IntervalSearchItem schema);
    
}
