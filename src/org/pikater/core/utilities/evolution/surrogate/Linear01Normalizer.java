/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.ontology.subtrees.newOption.typedValue.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;
import org.pikater.core.ontology.subtrees.search.searchItems.IntervalSearchItem;

/**
 *
 * @author Martin Pilat
 */
public class Linear01Normalizer extends ModelInputNormalizer {

    @Override
    public double normalizeFloat(ITypedValue dbl, IntervalSearchItem schema) {
        double min=((FloatValue)schema.getMin()).getValue();
        double range = ((FloatValue)schema.getMax()).getValue() - min;
        double val = ((FloatValue)dbl).getValue();
        val -= min;
        return val/range;
        
    }

    @Override
    public double normalizeInt(ITypedValue n, IntervalSearchItem schema) {
        double min=((IntegerValue)schema.getMin()).getValue();
        double range = ((IntegerValue)schema.getMax()).getValue() - min;
        double val = ((IntegerValue)n).getValue();
        val -= min;
        return val/range;
    }
    
}
