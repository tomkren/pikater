/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.utilities.evolution.FitnessFunction;
import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;
import org.pikater.shared.logging.core.ConsoleLogger;

import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 *
 * @author Martin Pilat
 */
public class SurrogateFitnessFunction implements FitnessFunction {

    Classifier surrogate;
    ModelInputNormalizer norm;
    
    public SurrogateFitnessFunction(Classifier surrogate, ModelInputNormalizer norm)  {
        this.surrogate = surrogate;
        this.norm = norm;
    }

    public void setSurrogate(Classifier surrogate) {
        this.surrogate = surrogate;
    }
    
    @Override
    public double evaluate(Individual ind) {
        SearchItemIndividual si = (SearchItemIndividual)ind;
        Instance in = si.toWekaInstance(norm);
        
        try {
            return surrogate.classifyInstance(in);
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
        
        return 0.0;
    }
    
    
}
