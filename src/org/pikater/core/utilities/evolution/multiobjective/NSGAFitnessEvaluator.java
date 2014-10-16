package org.pikater.core.utilities.evolution.multiobjective;

import java.util.List;
import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.individuals.Individual;

/**
 * Implementation of the NSGA-II fitness evaluation. 
 * 
 * The population is first divided into non-dominated fronts and then the crowding distance 
 * assignment is performed for each of the fronts. See [1] for details.
 * 
 * [1] Deb, Kalyanmoy, Amrit Pratap, Sameer Agarwal, and T. A. M. T. Meyarivan. "A fast and 
 * elitist multiobjective genetic algorithm: NSGA-II." Evolutionary Computation, IEEE 
 * Transactions on 6, no. 2 (2002): 182-197.
 * 
 * @author Martin Pilat
 *
 */
public class NSGAFitnessEvaluator extends MultiobjectiveFitnessEvaluator {

    @Override
    public void evaluate(Population pop) {
        double fitness = pop.getPopulationSize();
        List<Individual> toNDS = pop.getSortedIndividuals();

        List<List<Individual>> fronts = fastNonDominatedSort(toNDS);

        for (List<Individual> front : fronts) {
            crowdingDistanceAssignment(front);
            for (Individual chrom : front) {
                chrom.setFitnessValue(chrom.getFitnessValue() + fitness);
            }
            fitness--;
        }
    }
}
