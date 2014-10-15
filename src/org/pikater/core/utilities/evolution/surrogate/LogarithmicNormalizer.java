package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.searchItems.IntervalSearchItem;

/**
 * Performs a logarithmic transformation of the input values. First moves the interval in such a way that
 * the lower limit is 1.0. Than computes the logarithm of the result.
 * 
 * @author Martin Pilat
 */

public class LogarithmicNormalizer extends ModelInputNormalizer{

    @Override
    public double normalizeFloat(IValueData dbl, IntervalSearchItem schema) {
        double val = ((FloatValue) dbl).getValue();
        val -= ((FloatValue) schema.getMin()).getValue();
        val += 1.0;
        return Math.log(val);
    }

    @Override
    public double normalizeInt(IValueData n, IntervalSearchItem schema) {
        double val = ((IntegerValue) n).getValue();
        val -= ((IntegerValue) schema.getMin()).getValue();
        val += 1.0;
        return Math.log(val);
    }
    
}
