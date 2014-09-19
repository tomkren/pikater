package org.pikater.core.utilities.evolution.individuals;

import java.util.Arrays;

/**
 *
 * @author Martin Pilat
 */
public abstract class MultiobjectiveIndividual extends ArrayIndividual {
    float[] objectives;

    public MultiobjectiveIndividual() {
    }

    public float[] getObjectives() {
        return objectives;
    }

    public void setObjectives(float[] objectives) {
        this.objectives = objectives;
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
