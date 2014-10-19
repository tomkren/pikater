package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.search.searchItems.IntervalSearchItem;
import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;
import org.pikater.core.utilities.evolution.multiobjective.MultiobjectiveFitnessEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * Computes the target value for individuals as in the ASM-MOMA [1] algorithm. The non-dominated front
 * is extracted from the archive and the distance to the closest non-dominated individual is used as the 
 * target value.
 * 
 * [1] M. Pilat and R. Neruda. ASM-MOMA: Multiobjective memetic algorithm with aggregate
 * surrogate model. In Evolutionary Computation (CEC), 2011 IEEE Congress on, pages 1202-
 * 1208. IEEE, 2011.
 *      
 * @author Martin Pilat
 */
public class ASMMOMAModelValueProvider implements ModelValueProvider{

    List<SearchItemIndividual> nonDom = null;
    SearchItemIndividualArchive archive = null;
    
    /**
     * Computes the target value for individuals as in the ASM-MOMA algorithm. The non-dominated front
     * is extracted from the archive and the distance to the closest non-dominated individual is used as the 
     * target value.
     */
    @Override
    public double getModelValue(SearchItemIndividual si, SearchItemIndividualArchive archive, ModelInputNormalizer norm) {
        if (this.archive == null) {
            this.archive = archive;
            
            List<Individual> inds = new ArrayList<Individual>();
            inds.addAll(archive.getSavedIndividuals());
            
            List<Individual> tmp = MultiobjectiveFitnessEvaluator.getNonDominatedFront(inds);
            nonDom = new ArrayList<SearchItemIndividual>();
            
            for (Individual in : tmp) {
                nonDom.add((SearchItemIndividual)in);
            }
        }
        
        if (nonDom.contains(si)) {
            return 0.0;
        }
        
        return distanceToClosestNondominated(si);
    }

    /**
     * Computes the distance to the closes non-dominated individual.
     * @param si The individual for which the distance is computed
     * @return Distance of {@code si} to the non-dominated front.
     */
    double distanceToClosestNondominated(SearchItemIndividual si) {
        
        double min = Double.MAX_VALUE;
        
        for (SearchItemIndividual n : nonDom) {
            min = Math.min(distance(n, si), min);
        }
        
        return min;
    }
    
    
    /**
     * Computes the distance between to individuals. Euclidean distance is used for integer and double 
     * values (normalized to 0-1) and a binary distance is used for booleans (0 if both have the same value,
     * 1 otherwise)
     * 
     * @param i1 The first individual
     * @param i2 The second individual
     * @return The distance between {@code s1} and {@code s2}
     */
    double distance(SearchItemIndividual i1, SearchItemIndividual i2) {
        double dist = 0;
        
        for (int i = 0; i < i1.length(); i++) {
            IntervalSearchItem s1 = (IntervalSearchItem)i1.getSchema(i);
            if (s1.getMin() instanceof FloatValue) {
                double range = ((FloatValue)s1.getMax()).getValue() - ((FloatValue)s1.getMin()).getValue();
                dist += Math.pow(((FloatValue)i1.get(i)).getValue() - ((FloatValue)(i2.get(i))).getValue()/range, 2);
            } else if (s1.getMin() instanceof IntegerValue) {
                double range = ((IntegerValue)s1.getMax()).getValue() - ((IntegerValue)s1.getMin()).getValue();
                dist += Math.pow(((IntegerValue)i1.get(i)).getValue() - ((IntegerValue)(i2.get(i))).getValue()/range, 2);
                continue;
            } else {
                dist += i1.equals(i2) ? 0 : 1;
            }
        }
        
        return Math.log(Math.sqrt(dist) + 1);
    }
    
    @Override
    public void reset() {
        archive = null;
        nonDom = null;
    }
    
    
    
}
