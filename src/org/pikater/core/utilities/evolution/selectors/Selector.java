package org.pikater.core.utilities.evolution.selectors;

import org.pikater.core.utilities.evolution.Population;

/**
 *
 * @author Martin Pilat
 */
public interface Selector {

    public void select(int howMany, Population from, Population to);

}
