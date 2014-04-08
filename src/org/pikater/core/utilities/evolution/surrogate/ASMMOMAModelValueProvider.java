/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.utilities.evolution.surrogate;

import java.util.ArrayList;
import java.util.List;
import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;
import org.pikater.core.utilities.evolution.multiobjective.MultiobjectiveFitnessEvaluator;
import org.pikater.core.utilities.evolution.multiobjective.NSGAFitnessEvaluator;
import org.pikater.core.ontology.messages.FloatSItem;
import org.pikater.core.ontology.messages.IntSItem;
import org.pikater.core.ontology.messages.SearchItem;

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
            SearchItem s1 = i1.getSchema(i);
            if (s1 instanceof FloatSItem) {
                double range = ((FloatSItem)s1).getMax() - ((FloatSItem)s1).getMin();
                dist += Math.pow((Float.parseFloat(i1.get(i)) - Float.parseFloat(i2.get(i)))/range, 2);
                continue;
            }
            if (s1 instanceof IntSItem) {
                double range = ((IntSItem)s1).getMax() - ((IntSItem)s1).getMin();
                dist += Math.pow((Integer.parseInt(i1.get(i)) - Integer.parseInt(i2.get(i)))/range, 2);
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
