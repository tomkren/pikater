package org.pikater.core.utilities.evolution.multiobjective;

import java.io.Serializable;
import java.util.Comparator;

import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.MultiobjectiveIndividual;

public class ObjectiveValueComparator implements Comparator<Individual>, Serializable
{
	private static final long serialVersionUID = 5987378684140074409L;
	
	private int mult = 1;
    private int idx;

    public ObjectiveValueComparator(int idx)
    {
        this(idx, false);
    }

    public ObjectiveValueComparator(int idx, boolean reverse)
    {
        this.idx = idx;
        if (reverse)
        {
            mult = -1;
        }
    }

    @Override
    public int compare(Individual o1, Individual o2)
    {
        MultiobjectiveIndividual i1 = (MultiobjectiveIndividual) o1;
        MultiobjectiveIndividual i2 = (MultiobjectiveIndividual) o2;

        return mult * (int) Math.signum(i1.getObjectives()[idx] - i2.getObjectives()[idx]);
    }
}