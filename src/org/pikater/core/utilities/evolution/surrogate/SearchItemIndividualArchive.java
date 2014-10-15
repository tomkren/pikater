package org.pikater.core.utilities.evolution.surrogate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Implementation of the archive of {@link SearchItemIndividual}s. It is a wrapper for a {@link HashMap} with 
 * a few useful methods.
 */
public class SearchItemIndividualArchive {

    HashMap<String, SearchItemIndividual> archive = new HashMap<String, SearchItemIndividual>();
    
    /**
     * Adds the individual to the archive.
     * 
     * @param si The individual to add.
     */
    public void add(SearchItemIndividual si) {
       if (!archive.containsKey(si.toString())) {
           archive.put(si.toString(), (SearchItemIndividual)si.clone());
       }
    }
    
    /**
     * Checks whether a given individual exists in the archive.
     * 
     * @param si The individual
     * @return {@code true} if {@code si} is in the archive, {@code false} otherwise.
     */
    public boolean contains(SearchItemIndividual si) {
        return archive.containsKey(si.toString());
    }
    
    /**
     * Returns the fitness of the individual (if it is stored in the archive).
     * 
     * @param si The individual
     * @return The cached value of the fitness of {@code si}
     */
    public double getFitness(SearchItemIndividual si) {
        return archive.get(si.toString()).getFitnessValue();
    }
    
    /**
     * Returns the size of the archive
     * 
     * @return the size of the archive
     */
    public int size() {
        return archive.size();
    }
    
    /**
     * Return the individuals from the archive as an {@link ArrayList}.
     * @return
     */
    
    public ArrayList<SearchItemIndividual> getSavedIndividuals() {
        return new ArrayList<SearchItemIndividual>(archive.values());
    }
    
    /**
     * Creates weka {@link Instances} from the archive, so the individuals can be used for the training of 
     * a surrogate model.
     * 
     * @param mvp Provider of the model value
     * @param norm Normalizer of the inputs
     * @return The archive transformed to a dataset, which can be used to train the model.
     */
    public Instances getWekaDataSet(ModelValueProvider mvp, ModelInputNormalizer norm) {
        
        mvp.reset();
        
        Collection<SearchItemIndividual> inds = archive.values();
        
        if (inds.isEmpty()) {
            return null;
        }
        
        java.util.Iterator<SearchItemIndividual> it = inds.iterator();
        SearchItemIndividual first = it.next();
        
        Instances inst = first.emptyDatasetFromSchema();
        Instance firstInstance = first.toWekaInstance(norm);
        firstInstance.setClassValue(mvp.getModelValue(first, this, norm));
        firstInstance.setDataset(inst);
        inst.add(firstInstance);
        
        while (it.hasNext()) {
            SearchItemIndividual ind = it.next();
            Instance in = ind.toWekaInstance(norm);
            in.setClassValue(mvp.getModelValue(ind, this, norm));
            in.setDataset(inst);
            inst.add(in);
        }
        
        return inst;
    }
}