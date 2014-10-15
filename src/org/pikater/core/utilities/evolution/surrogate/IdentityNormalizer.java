package org.pikater.core.utilities.evolution.surrogate;


import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.search.searchItems.IntervalSearchItem;

/**
 * Dummy normalizer which does not do any normalization. Can be used to switch normalization off.
 * @author Martin Pilat
 */
public class IdentityNormalizer extends ModelInputNormalizer {

    //TODO: not necessary now imho, remove
    @Override
    public double normalizeFloat(IValueData dbl, IntervalSearchItem schema) {
        return ((FloatValue)dbl).getValue();
    }

    @Override
    public double normalizeInt(IValueData n, IntervalSearchItem schema) {
        return ((IntegerValue)n).getValue();
    }
    
}
