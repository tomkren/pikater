package org.pikater.core.utilities.evolution.surrogate;

import org.pikater.core.utilities.evolution.EvolutionaryAlgorithm;
import org.pikater.core.utilities.evolution.MergingReplacement;
import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.RandomNumberGenerator;
import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;
import org.pikater.core.utilities.evolution.operators.OnePtXOver;
import org.pikater.core.utilities.evolution.operators.Operator;
import org.pikater.core.utilities.evolution.operators.SearchItemIndividualMutation;
import org.pikater.core.utilities.evolution.selectors.TournamentSelector;
import org.pikater.shared.logging.core.ConsoleLogger;

import weka.classifiers.functions.GaussianProcesses;
import weka.core.Instances;

/**
 * Informed mutation based on the information from a surrogate model (Gaussian process regression).
 * Performs an internal evolutionary search to find an optimum of the model. 
 * 
 * Details can be found in: M. Pilat and R. Neruda. ASM-MOMA: Multiobjective memetic algorithm
 * with aggregate surrogate model. In Evolutionary Computation (CEC), 2011 IEEE Congress on,
 * pages 1202-1208. IEEE, 2011.
 * 
 * @author Martin Pilat
 */
public class SurrogateMutationOperator implements Operator {

    SearchItemIndividualArchive archive;
    double mutProbability;
    ModelValueProvider mvp;
    ModelInputNormalizer norm;

    /**
     * Constructor. Creates an instance of the mutation. 
     * 
     * @param archive Archive of previously evaluated individuals
     * @param mutProbability Probability of mutation
     * @param mvp A provider for the value of model
     * @param norm Normalizer for the input values
     */
    public SurrogateMutationOperator(SearchItemIndividualArchive archive,
        double mutProbability, ModelValueProvider mvp,
        ModelInputNormalizer norm) {
        this.archive = archive;
        this.mutProbability = mutProbability;
        this.mvp = mvp;
        this.norm = norm;
    }

    /**
     * Performs the mutation. First the individuals from the archive are used to create the values for the mode,
     * which are then normalized using the normalizer. Gaussian process regression model is used to 
     * predict the values of the model. An internal evolutionary algorithm is started to find a good
     * value of the model. The solution found by the internal algorithm is added back to the population.
     */
    @Override
    public void operate(Population parents, Population offspring) {
        try {
            
            if (archive.size() < 20) {
                offspring.addAll(parents);
                return;
            }
            
            Instances train = archive.getWekaDataSet(mvp, norm);
            GaussianProcesses smo = new GaussianProcesses();
            smo.buildClassifier(train);

            for (int i = 0; i < parents.getPopulationSize(); i++) {
                if (RandomNumberGenerator.getInstance().nextDouble() > mutProbability) {
                    offspring.add((SearchItemIndividual)parents.get(i).clone());
                    continue;
                }

                Population innerPopulation = new Population();
                for (int j = 0; j < 20; j++) {
                    innerPopulation.add(parents.get(i));
                }

                SearchItemIndividualMutation siim = new SearchItemIndividualMutation(1.0, 0.25, 0.1);
                
                Population perturbed = new Population();
                siim.operate(parents, perturbed);
                    
                innerPopulation.addAll(perturbed);
                innerPopulation.add(parents.get(i));
                innerPopulation.setSampleIndividual((SearchItemIndividual) parents.get(0).clone());

                EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm();

                ea.addOperator(new OnePtXOver(0.8));
                ea.addOperator(new SearchItemIndividualMutation(0.8, 0.5, 0.05));
                ea.addEnvironmentalSelector(new TournamentSelector());
                ea.setReplacement(new MergingReplacement());

                ea.setElite(0.1);
                ea.setFitnessFunction(new SurrogateFitnessFunction(smo,norm));
                
                for (int j = 0; j < 10; j++) {
                    ea.evolve(innerPopulation);
                }
              
                SearchItemIndividual bestIndividual = (SearchItemIndividual)
                    innerPopulation.getSortedIndividuals().get(0);
                offspring.add(bestIndividual);              
            }


        } catch (Exception e) {
            ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }



    }
}
