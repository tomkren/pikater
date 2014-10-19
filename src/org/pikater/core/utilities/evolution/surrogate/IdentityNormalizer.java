package org.pikater.core.utilities.evolution.surrogate;


import org.pikater.core.ontology.subtrees.newoption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.searchitems.IntervalSearchItem;

/**
 * Dummy normalizer which does not do any normalization. Can be used to switch normalization off.
 * @author Martin Pilat
 */
public class IdentityNormalizer extends ModelInputNormalizer {

    @Override
    public double normalizeFloat(IValueData dbl, IntervalSearchItem schema) {
        return ((FloatValue)dbl).getValue();
    }

    @Override
    public double normalizeInt(IValueData n, IntervalSearchItem schema) {
        return ((IntegerValue)n).getValue();
    }
    
}
