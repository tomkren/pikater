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
public class IdentityNormalizer extends ModelInputNormalizer {

    @Override
    public double normalizeFloat(String dbl, FloatSItem schema) {
        return Float.parseFloat(dbl);
    }

    @Override
    public double normalizeInt(String n, IntSItem schema) {
        return Integer.parseInt(n);
    }
    
}
