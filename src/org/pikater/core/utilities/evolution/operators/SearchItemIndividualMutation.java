package org.pikater.core.utilities.evolution.operators;

import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.search.searchItems.IntervalSearchItem;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;
import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.RandomNumberGenerator;
import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;
import org.pikater.core.ontology.subtrees.search.searchItems.SetSItem;

/**
 *
 * A mutation operator for individuals represented as a vector of {@link SearchItem}s. Goes through
 * the individual and sets a new random value for each of the {@link SearchItem}s with a given probability.
 *
 * @author Martin Pilat
 */
public class SearchItemIndividualMutation implements Operator {

    double mutationProbability;
    double geneChangeProbability;
    double changeWidth;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();

    /**Cosntructor
     * 
     * @param mutationProbability Probability of mutation for a given individual
     * @param geneChangeProbability Probability of gene change in a mutated individual
     * @param changeWidth How much integer and float parameters should be changed (as a ratio of the width of their interval)
     */
    public SearchItemIndividualMutation(double mutationProbability,
    		double geneChangeProbability, double changeWidth) {
        this.mutationProbability = mutationProbability;
        this.geneChangeProbability = geneChangeProbability;
        this.changeWidth = changeWidth;
    }
    
    @Override
    public void operate(Population parents, Population offspring) {
        
        int size = parents.getPopulationSize();

        for (int i = 0; i < size; i++) {

             SearchItemIndividual p1 = (SearchItemIndividual) parents.get(i);
             SearchItemIndividual o1 = (SearchItemIndividual) p1.clone();

             if (rng.nextDouble() < mutationProbability) {
                 for (int j = 0; j < o1.length(); j++) {
                     if (rng.nextDouble() < geneChangeProbability) {
                         if (o1.getSchema(j) instanceof SetSItem) {
                            o1.set(j, p1.getSchema(j).randomValue(rng.getRandom()));
                         }
                         if (o1.getSchema(j) instanceof IntervalSearchItem) {
                             IntervalSearchItem sItem=(IntervalSearchItem)o1.getSchema(j);
                             if (sItem.getMin() instanceof FloatValue)
                             {
                                 float val = ((FloatValue)o1.get(j)).getValue();
                                 float min= ((FloatValue) sItem.getMin()).getValue();
                                 float max= ((FloatValue) sItem.getMax()).getValue();
                                 val += changeWidth*(max-min)*rng.nextGaussian();
                                 val = Math.min(val, max);
                                 val = Math.max(val, min);
                                 o1.set(j, new FloatValue(val));
                             }
                             else if (sItem.getMin() instanceof IntegerValue)
                             {
                                 int val = ((IntegerValue)o1.get(j)).getValue();
                                 int min= ((IntegerValue) sItem.getMin()).getValue();
                                 int max= ((IntegerValue) sItem.getMax()).getValue();
                                 val += changeWidth*(max-min)*rng.nextGaussian();
                                 val = Math.min(val, max);
                                 val = Math.max(val, min);
                                 o1.set(j, new IntegerValue(val));
                             }

                         }
                     }
                 }
             }

             offspring.add(o1);
        }
        
        
        
    }
    
}
