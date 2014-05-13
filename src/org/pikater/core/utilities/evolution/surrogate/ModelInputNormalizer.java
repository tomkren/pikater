/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.ontology.messages.IntSItem;
import org.pikater.core.ontology.messages.searchItems.FloatSItem;

/**
 *
 * @author Martin Pilat
 */
public abstract class ModelInputNormalizer {
    
    public abstract double normalizeFloat(String dbl, FloatSItem schema);
    public abstract double normalizeInt(String n, IntSItem schema);
    
}
