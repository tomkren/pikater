package org.pikater.core.utilities.evolution.multiobjective;

import java.io.Serializable;
import java.util.Comparator;

import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.MultiobjectiveIndividual;


/**
 * Compares two individuals based on one of the objectives.
 *
 */
public class ObjectiveValueComparator implements Comparator<Individual>, Serializable {
	private static final long serialVersionUID = 5987378684140074409L;
	
	private int mult = 1;
    private int idx;

    
    /**
     * Constructor, sets the index of the objective which is used for comparison.
     * 
     * @param idx the index of the objective which is used for comparison
     */
    public ObjectiveValueComparator(int idx) {
        this(idx, false);
    }

    /**
     * Constructor, sets the index of the objective which is used for comparison and also 
     * allows reverse sorting
     * 
     * @param idx the index of the objective which is used for comparison
     * @param reverse whether the individuals should be sorted in ascending {@code false} or 
     * descending  {@code true} order
     */
    
    public ObjectiveValueComparator(int idx, boolean reverse) {
        this.idx = idx;
        if (reverse) {
            mult = -1;
        }
    }

    @Override
    public int compare(Individual o1, Individual o2) {
        MultiobjectiveIndividual i1 = (MultiobjectiveIndividual) o1;
        MultiobjectiveIndividual i2 = (MultiobjectiveIndividual) o2;

        return mult * (int) Math.signum(i1.getObjectives()[idx] - i2.getObjectives()[idx]);
    }
}