package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;

/**
 * Provides the fitness of the individual from the archive as the fitness value. Can be
 * used in single-objective case to model the fitness directly without any transformations.
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
