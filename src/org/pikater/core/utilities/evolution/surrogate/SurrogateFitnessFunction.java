package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.utilities.evolution.FitnessFunction;
import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;
import org.pikater.shared.logging.core.ConsoleLogger;

import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 * Fitness function which uses a model to predict the value instead of evaluating it directly.
 * 
 * @author Martin Pilat
 */
public class SurrogateFitnessFunction implements FitnessFunction {

    Classifier surrogate;
    ModelInputNormalizer norm;
    
    /**
     * Creates an instance of this class with two parameters - the surrogate model and normalizer.s
     * 
     * @param surrogate The surrogate model
     * @param norm The normalizer
     */
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
