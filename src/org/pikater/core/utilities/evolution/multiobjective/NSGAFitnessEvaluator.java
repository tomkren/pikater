package org.pikater.core.utilities.evolution.multiobjective;

import java.util.List;
import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.individuals.Individual;

public class NSGAFitnessEvaluator extends MultiobjectiveFitnessEvaluator {

    private static final long serialVersionUID = 687521583781927562L;

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
