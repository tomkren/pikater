/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.utilities.evolution.selectors;

import java.util.ArrayList;
import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.individuals.Individual;

/**
 *
 * @author Martin Pilat
 */
public class BestIndividualsSelector implements Selector{

    @Override
    public void select(int howMany, Population from, Population to) {
        
        ArrayList<Individual> sorted = from.getSortedIndividuals();
        
        for (int i = 0; i < howMany; i++) {
            to.add((Individual)sorted.get(i).clone());
        }
        
        
    }
    
    
}
