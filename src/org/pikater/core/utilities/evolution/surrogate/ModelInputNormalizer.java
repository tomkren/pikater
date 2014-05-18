/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.ontology.subtrees.search.searchItems.FloatSItem;
import org.pikater.core.ontology.subtrees.search.searchItems.IntSItem;

/**
 *
 * @author Martin Pilat
 */
public abstract class ModelInputNormalizer {
    
    public abstract double normalizeFloat(String dbl, FloatSItem schema);
    public abstract double normalizeInt(String n, IntSItem schema);
    
}
