package org.pikater.core.utilities.evolution.operators;

import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.RandomNumberGenerator;
import org.pikater.core.utilities.evolution.individuals.IntegerIndividual;

/**
 * A mutation for integer encoded individuals. Goes through the indivudal and generates new value from the 
 * valid interval for each of the positions with a given probability.
 *
 * @author Martin Pilat
 */
public class IntegerMutation implements Operator {

    double mutationProbability;
    double geneChangeProbability;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();

    /**
     * Constructor, sets the probabilities
     * 
     * @param mutationProbability the probability of mutating an individual
     * @param geneChangeProbability the probability of changing a given gene in the mutated individual
     */
    
    public IntegerMutation(double mutationProbability, double geneChangeProbability) {
        this.mutationProbability = mutationProbability;
        this.geneChangeProbability = geneChangeProbability;
    }

    public void operate(Population parents, Population offspring) {

        int size = parents.getPopulationSize();

        for (int i = 0; i < size; i++) {

             IntegerIndividual p1 = (IntegerIndividual) parents.get(rng.nextInt(size));
             IntegerIndividual o1 = (IntegerIndividual) p1.clone();

             if (rng.nextDouble() < mutationProbability) {
                 mutate(o1);
             }

             offspring.add(o1);
        }
    }

    /**
     * Performs the actual mutation
     * @param o1 The offspring to mutate
     */
    private void mutate(IntegerIndividual o1) {
        for (int j = 0; j < o1.length(); j++) {
             if (rng.nextDouble() < geneChangeProbability) {
                 int value = o1.getMax() - o1.getMin();
                 int val0 = RandomNumberGenerator.getInstance().nextInt(value) + o1.getMin();
                 IntegerValue valueO = new IntegerValue(val0);
                 o1.set(j, valueO);
             }
         }
    }

}
