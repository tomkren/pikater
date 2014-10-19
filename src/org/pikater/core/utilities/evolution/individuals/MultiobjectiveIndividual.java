package org.pikater.core.utilities.evolution.individuals;

import java.util.Arrays;

/**
 * An implementation of individual which can be used in multi-objective optimization. Stores an array of
 * fitness values instead of only one.
 *  
 * @author Martin Pilat
 */
public abstract class MultiobjectiveIndividual extends ArrayIndividual {
    float[] objectives;

    public MultiobjectiveIndividual() {
    }

    /**
     * Returns the saved objective values
     * @return the saved objective values
     */
    
    public float[] getObjectives() {
        return Arrays.copyOf(objectives, objectives.length);
    }

    /**
     * Sets the objective values
     * @param objectives the objective values to save
     */
    
    public void setObjectives(float[] objectives) {
        this.objectives = Arrays.copyOf(objectives, objectives.length);
    }
    
    @Override
    public MultiobjectiveIndividual clone() {
        MultiobjectiveIndividual mi = (MultiobjectiveIndividual)super.clone();
        
        if (this.objectives != null) {
            mi.objectives = Arrays.copyOf(this.objectives, this.objectives.length);
        }
        
        return mi;
    }
}

