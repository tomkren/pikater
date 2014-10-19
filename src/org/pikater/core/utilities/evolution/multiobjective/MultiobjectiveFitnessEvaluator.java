package org.pikater.core.utilities.evolution.multiobjective;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pikater.core.utilities.evolution.FitnessEvaluator;
import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.MultiobjectiveIndividual;

/**
 * Basic class for multi-objective fitness evaluations. Provides the interface and some
 * utility methods (non-dominated front extraction, non-dominated sorting and crowding
 * distance assignment).
 * 
 * @author Martin Pilat
 */

public abstract class MultiobjectiveFitnessEvaluator implements FitnessEvaluator {

    /**
     * Extracts the non-dominated individuals from the list of individuals 
     *
     * @param pop Population from which the non-dominated front is chosen
     * @return List of Individuals in current non-dominated front
     */
    public static List<Individual> getNonDominatedFront(List<Individual> population) {
        List<Individual> front = new ArrayList<Individual>();
        front.add(population.get(0));

        for (Individual p : population) {
            if (!front.contains(p)) {
	            front.add(p);
	            List<Individual> toRemove = new ArrayList<Individual>();
	            for (Individual q : front) {
	                if (!q.equals(p) && !toRemove.contains(q)) { 
	                	if (dominates(q, p)) {
		                    toRemove.add(p);
		                } else if (dominates(p, q)) {
		                    toRemove.add(q);
		                }
	                }
	            }
	            front.removeAll(toRemove);
            }
        }
        return front;
    }

    /**
     * Performs fast non-dominated sorting (division of population into non-dominated fronts).
     * 
     * @param pop the population of individuals
     * @return The list of non-dominated fronts, lower fronts are first
     */
    
    protected List<List<Individual>> fastNonDominatedSort(List<Individual> pop) {
        List<List<Individual>> fronts = new ArrayList<List<Individual>>();
        List<Individual> population = new ArrayList<Individual>();
        population.addAll(pop);
        while (!population.isEmpty()) {
            List<Individual> front = getNonDominatedFront(population);
            fronts.add(front);
            population.removeAll(front);
        }
        return fronts;
    }

    /**
     * Assigns the crowding distance to all individuals in a single non-dominated front 
     * as used e.g. in the NSGA-II algorithm.
     * 
     * @param front the non-dominated front
     */
    
    protected void crowdingDistanceAssignment(List<Individual> front) {
        List<MultiobjectiveIndividual> mi = new ArrayList<MultiobjectiveIndividual>();
        for (Individual ind : front) {
            mi.add((MultiobjectiveIndividual) ind);
        }
        int l = front.size();
        for (Individual i : front) {
            i.setFitnessValue(0.0);
        }

        for (int j = 0; j < mi.get(0).getObjectives().length; j++) {
            Collections.sort(mi, new ObjectiveValueComparator(j));
            mi.get(0).setFitnessValue(Double.POSITIVE_INFINITY);
            mi.get(l - 1).setFitnessValue(Double.POSITIVE_INFINITY);
            for (int i = 1; i < l - 1; i++) {
                double dist = mi.get(i).getFitnessValue();
                dist += mi.get(i + 1).getObjectives()[j];
                dist -= mi.get(i - 1).getObjectives()[j];
                front.get(i).setFitnessValue(dist);
            }
        }
        Collections.sort(front, new FitnessValueComparator());
        double diff = 0.5 / front.size();
        double fit = 1.0;
        for (Individual chrom : front) {
            chrom.setFitnessValue(fit);
            fit -= diff;
        }
    }

    /**
     * Compares two Individuals for domination
     *
     * @param a first Individual to compare
     * @param b second Individual to compare
     * @return true if Individual a dominates Individual b
     */
    public static boolean dominates(Individual a, Individual b) {
        MultiobjectiveIndividual i1 = (MultiobjectiveIndividual) a;
        MultiobjectiveIndividual i2 = (MultiobjectiveIndividual) b;
        int domCount = 0;
        for (int i = 0; i < i1.getObjectives().length; i++) {
            if (i1.getObjectives()[i] <= i2.getObjectives()[i]) {
                domCount++;
            }
        }
        if (domCount == i1.getObjectives().length) {
            return true;
        }
        return false;
    }

    /**
     * Compares two individuals based on their fitness value.
     *
     */
    protected static class FitnessValueComparator implements Comparator<Individual>, Serializable {


        private static final long serialVersionUID = 1L;

        @Override
        public int compare(Individual o1, Individual o2) {
            return (int) Math.signum(o2.getFitnessValue() - o1.getFitnessValue());
        }
    }
}