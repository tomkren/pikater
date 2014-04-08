/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;

/**
 *
 * @author Martin Pilat
 */
public class FitnessModelValueProvider implements ModelValueProvider {

    @Override
    public double getModelValue(SearchItemIndividual si, SearchItemIndividualArchive archive, ModelInputNormalizer norm) {
        return archive.getFitness(si);
    }

    @Override
    public void reset() {       
    }
    
}
