package org.pikater.core.utilities.evolution.selectors;

import java.util.List;

import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.individuals.Individual;

/**
 * A selector which selects the best individuals according to their fitness. 
 * 
 * @author Martin Pilat
 */
public class BestIndividualsSelector implements Selector{

    @Override
    public void select(int howMany, Population from, Population to) {
        
        List<Individual> sorted = from.getSortedIndividuals();
        
        for (int i = 0; i < howMany; i++) {
            to.add((Individual)sorted.get(i).clone());
        }
        
        
    }
    
    
}
