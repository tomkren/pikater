package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.ontology.subtrees.newoption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.searchitems.IntervalSearchItem;

/**
 * Normalizes the inputs linearly to the 0-1 range.
 * 
 * @author Martin Pilat
 */
public class Linear01Normalizer extends ModelInputNormalizer {

    @Override
    public double normalizeFloat(IValueData dbl, IntervalSearchItem schema) {
        double min=((FloatValue)schema.getMin()).getValue();
        double range = ((FloatValue)schema.getMax()).getValue() - min;
        double val = ((FloatValue)dbl).getValue();
        val -= min;
        return val/range;
        
    }

    @Override
    public double normalizeInt(IValueData n, IntervalSearchItem schema) {
        double min=((IntegerValue)schema.getMin()).getValue();
        double range = ((IntegerValue)schema.getMax()).getValue() - min;
        double val = ((IntegerValue)n).getValue();
        val -= min;
        return val/range;
    }
    
}
