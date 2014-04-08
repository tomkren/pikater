package org.pikater.core.utilities.evolution.operators;

import org.pikater.core.utilities.evolution.Population;

/**
 *
 * @author Martin Pilat
 */
public interface Operator {

    public void operate(Population parents, Population offspring);

}
