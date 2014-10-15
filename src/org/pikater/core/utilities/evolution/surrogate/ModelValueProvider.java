package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;

/**
 * Interface for model value providers. They are used to calculate the targe value of each individual
 * with respect to other individuals in the archive. This value is used as a target for a surrogate model.
 * @author Martin Pilat
 */
public interface ModelValueProvider {
    
	/**
	 * Calculates the value of the individual with respect to the rest of the archive.
	 * @param si The individual
	 * @param archive The archive of the other individuals
	 * @param norm The input normalizer
	 * @return The target value for the individual
	 */
    public double getModelValue(SearchItemIndividual si, SearchItemIndividualArchive archive, ModelInputNormalizer norm);
    
    /**
     * Reinitilizes the value provider.
     */
    public void reset();
}
