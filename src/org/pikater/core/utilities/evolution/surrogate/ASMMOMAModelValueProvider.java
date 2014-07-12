/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.ontology.subtrees.newOption.typedValue.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;
import org.pikater.core.ontology.subtrees.search.searchItems.IntervalSearchItem;
import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;
import org.pikater.core.utilities.evolution.multiobjective.MultiobjectiveFitnessEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Pilat
 */
public class ASMMOMAModelValueProvider implements ModelValueProvider{

    ArrayList<SearchItemIndividual> nonDom = null;
    SearchItemIndividualArchive archive = null;
    ModelInputNormalizer norm = null;
    
    @Override
    public double getModelValue(SearchItemIndividual si, SearchItemIndividualArchive archive, ModelInputNormalizer norm) {
        if (this.archive == null) {
            this.archive = archive;
            this.norm = norm;
            
            ArrayList<Individual> inds = new ArrayList<Individual>();
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

    double distanceToClosestNondominated(SearchItemIndividual si) {
        
        double min = Double.MAX_VALUE;
        
        for (SearchItemIndividual n : nonDom) {
            min = Math.min(distance(n, si), min);
        }
        
        return min;
    }
    
    double distance(SearchItemIndividual i1, SearchItemIndividual i2) {
        double dist = 0;
        
        for (int i = 0; i < i1.length(); i++) {
            IntervalSearchItem s1 = (IntervalSearchItem)i1.getSchema(i);
            if (s1.getMin() instanceof FloatValue) {
                double range = ((FloatValue)s1.getMax()).getValue() - ((FloatValue)s1.getMin()).getValue();
                dist += Math.pow(((FloatValue)i1.get(i)).getValue() - ((FloatValue)(i2.get(i))).getValue()/range, 2);
                continue;
            }
            if (s1.getMin() instanceof IntegerValue) {
                double range = ((IntegerValue)s1.getMax()).getValue() - ((IntegerValue)s1.getMin()).getValue();
                dist += Math.pow(((IntegerValue)i1.get(i)).getValue() - ((IntegerValue)(i2.get(i))).getValue()/range, 2);
                continue;
            }
            dist += i1.equals(i2) ? 0 : 1;
        }
        
        return Math.log(Math.sqrt(dist) + 1);
    }
    
    @Override
    public void reset() {
        archive = null;
        nonDom = null;
    }
    
    
    
}
