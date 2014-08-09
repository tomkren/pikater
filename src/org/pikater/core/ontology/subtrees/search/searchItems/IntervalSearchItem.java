package org.pikater.core.ontology.subtrees.search.searchItems;

import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Kuba
 * Date: 9.7.2014
 * Time: 17:26
 */
public class IntervalSearchItem extends SearchItem {
    private static final long serialVersionUID = -8936246832263259542L;
    private IValueData min;
    private IValueData max;
    public IValueData getMin() {
        return min;
    }
    public void setMin(IValueData min) {
        this.min = min;
    }
    public IValueData getMax() {
        return max;
    }
    public void setMax(IValueData max) {
        this.max = max;
    }

        @Override
    public List<IValueData> possibleValues() {
        List<IValueData> posVals =new ArrayList<IValueData>();
        if (min instanceof BooleanValue)
        {
            if (min.equals(max))
            {
                posVals.add(min);
            }
            else {
                posVals.add(new BooleanValue(false));
                posVals.add(new BooleanValue(true));
            }
        }
        else if (min instanceof FloatValue)
        {
            float floatMin=((FloatValue)min).getValue();
            float floatMax=((FloatValue)max).getValue();
            int x = getNumber_of_values_to_try();
            float dv = (floatMax - floatMin)/ (x - 1);
            for (int i = 0; i < x; i++) {
                float vFloat = floatMin + i * dv;
                posVals.add(new FloatValue(vFloat));
            }
        }
        else if (min instanceof DoubleValue)
        {
            double doubleMin=((DoubleValue)min).getValue();
            double doubleMax=((DoubleValue)max).getValue();
            int x = getNumber_of_values_to_try();
            double dv = (doubleMax - doubleMin)/ (x - 1);
            for (int i = 0; i < x; i++) {
                double vFloat = doubleMin + i * dv;
                posVals.add(new DoubleValue(vFloat));
            }
        }
            else if (min instanceof IntegerValue)
        {
            int x = getNumber_of_values_to_try();
            int intMin=((IntegerValue)min).getValue();
            int intMax=((IntegerValue)max).getValue();
            int range = (intMax	-  intMin + 1);
            // if there is less possibilities than x -> change x
            if (range < x) {
                x = range;
            }
            for (int i = 0; i < x; i++) {
                int vInt = intMin + i	* (range / x);
                posVals.add(new IntegerValue(vInt));
            }
        }
        return posVals;
    }
}
