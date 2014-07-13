package org.pikater.core.agents.experiment.search;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newOption.NewOptionList;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.search.SearchSolution;
import org.pikater.core.ontology.subtrees.search.searchItems.SearchItem;
import org.pikater.core.options.EASearch_SearchBox;
import org.pikater.core.utilities.evolution.*;
import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.SearchItemIndividual;
import org.pikater.core.utilities.evolution.multiobjective.NSGAFitnessEvaluator;
import org.pikater.core.utilities.evolution.operators.OnePtXOver;
import org.pikater.core.utilities.evolution.operators.Operator;
import org.pikater.core.utilities.evolution.operators.SearchItemIndividualMutation;
import org.pikater.core.utilities.evolution.selectors.BestIndividualsSelector;
import org.pikater.core.utilities.evolution.selectors.Selector;
import org.pikater.core.utilities.evolution.selectors.TournamentSelector;
import org.pikater.core.utilities.evolution.surrogate.ASMMOMAModelValueProvider;
import org.pikater.core.utilities.evolution.surrogate.FitnessModelValueProvider;
import org.pikater.core.utilities.evolution.surrogate.IdentityNormalizer;
import org.pikater.core.utilities.evolution.surrogate.LogarithmicNormalizer;
import org.pikater.core.utilities.evolution.surrogate.SearchItemIndividualArchive;
import org.pikater.core.utilities.evolution.surrogate.SurrogateMutationOperator;

public class Agent_EASearch extends Agent_Search {
    /*
     * Implementation of Genetic algorithm search
     * Half uniform crossover, tournament selection
     * Options:
     * -E float
     * minimum error rate (default 0.1)
     * 
     * -M int 
     * maximal number of generations (default 10)
     * 
     * -I int
     * maximal number of evaluated configurations (default 100)
     * 
     * -T float
     * Mutation rate (default 0.2)
     * 
     * -F float
     * Mutation rate per field in individual (default 0.2)
     * 
     * -X float
     * Crossover probability (default 0.5)
     * 
     * -P int
     * population size (default 10)
     * 
     * -L float
     * The percentage of elite individuals (default 0.1)
     * 
     * 
     */

    //fitness is the error rate - the lower, the better!
    SearchItemIndividualArchive archive = new SearchItemIndividualArchive();
    Population parents;
    Population offspring;
    Population toEvaluate = new Population();
    Population evaluated = new Population();
    Replacement replacement = new MergingReplacement();
    ArrayList<Selector> environmentalSelectors;
    ArrayList<Selector> matingSelectors;
    ArrayList<Operator> operators;
    boolean multiobjective = true;
    boolean surrogate = false;
    double eliteSize = 0.1;
    int popSize = 10;
    double mutProb = 0.0;
    double mutProbPerField = 0.0;
    double xOverProb = 0.0;
    private int genNumber = 0;
    private double bestError = Double.MAX_VALUE;
    private int maxGeneration = 5;
    private double goalError = 0.1;
    private int maxEval = 100;
    protected Random rng = RandomNumberGenerator.getInstance().getRandom();
    /**
     *
     */
    private static final long serialVersionUID = -387458001824777077L;
    

    @Override
    protected String getAgentType() {
        return "EASearch";
    }
    
	@Override
	protected AgentInfo getAgentInfo() {

		return EASearch_SearchBox.get();
	}

    @Override
    protected boolean finished() {
        //number of generations, best error rate
        
        if (genNumber >= maxGeneration) {
            return true;
        }

        if (bestError <= goalError) {
            return true;
        }
        
        if (archive.size() >= maxEval) 
            return true;
        
        return false;
    }
    
    @Override
    protected List<SearchSolution> generateNewSolutions(List<SearchSolution> solutions, float[][] evaluations) {

        offspring = new Population();
        offspring.setPopulationSize(popSize);
        
        genNumber++;
        
        if (evaluations == null) {
            //create new population
            
            matingSelectors = new ArrayList<Selector>();
            environmentalSelectors = new ArrayList<Selector>();
            operators = new ArrayList<Operator>();
            archive = new SearchItemIndividualArchive();
            
            if (!multiobjective) {
                environmentalSelectors.add(new TournamentSelector());            
            }
            else {
                environmentalSelectors.add(new BestIndividualsSelector());
                eliteSize = 0.0;
            }
                
            operators.add(new OnePtXOver(xOverProb));
            operators.add(new SearchItemIndividualMutation(mutProb, mutProbPerField, 0.3));
            
            if (surrogate && !multiobjective) {
                operators.add(new SurrogateMutationOperator(archive, 0.25, new FitnessModelValueProvider(), new IdentityNormalizer()));
            }
            
            if (surrogate && multiobjective) {
                operators.add(new SurrogateMutationOperator(archive, 0.25, new ASMMOMAModelValueProvider(), new LogarithmicNormalizer()));
            }
            
            parents = new Population();
            parents.setPopulationSize(popSize);
            
            List<SearchItem> schema = getSchema();
            
            SearchItemIndividual sampleIndividual = new SearchItemIndividual(schema.size());
            
            for (int i = 0; i < schema.size(); i++) {
                sampleIndividual.set(i, "");
                sampleIndividual.setSchema(i, schema.get(i));
            }
            
            parents.setSampleIndividual(sampleIndividual);
            
            genNumber = 0;
            bestError = Double.MAX_VALUE;
            parents.createRandomInitialPopulation();
            
            return populationToList(parents);
        }
        
        Population matingPool = new Population();

        if (matingSelectors.size() > 0) {
            int mateSel = matingSelectors.size();
            int toSelect = parents.getPopulationSize()/mateSel;
            for (int i = 0; i < matingSelectors.size(); i++) {
                Population sel = new Population();
                matingSelectors.get(i).select(toSelect, parents, sel);
                matingPool.addAll((Population)sel.clone());
            }

            int missing = parents.getPopulationSize() - matingPool.getPopulationSize();
            if (missing > 0) {
                Population sel = new Population();
                matingSelectors.get(matingSelectors.size()-1).select(toSelect, parents, sel);
                matingPool.addAll((Population)sel.clone());
            }
        } else
        {
            matingPool = (Population)parents.clone();
        }
        
        offspring = null;
        for (Operator o : operators) {
            offspring = new Population();
            o.operate(matingPool, offspring);
            matingPool = offspring;
        }
        
        toEvaluate.clear();
        evaluated.clear();
        
        for (int i = 0; i < offspring.getPopulationSize(); i++) {
            if (archive.contains((SearchItemIndividual)offspring.get(i))) {
                offspring.get(i).setFitnessValue(archive.getFitness((SearchItemIndividual)offspring.get(i)));
                evaluated.add(offspring.get(i));
                continue;
            }
            toEvaluate.add(offspring.get(i));
        }
        
        return populationToList(toEvaluate);
        
    }

    private List<SearchSolution> populationToList(Population pop) {
        
        List<SearchSolution> ret = new ArrayList<SearchSolution>();
        for (Individual i : pop.getSortedIndividuals()) {
            SearchItemIndividual si = (SearchItemIndividual)i;
            List<String> vals = new ArrayList<String>();
            
            for (int j = 0; j < si.length(); j++) {
                vals.add(si.get(j).toString());
            }
            
            SearchSolution ss = new SearchSolution();
            ss.setValues(vals);
            ret.add(ss);
        }
        return ret;
    }
    
    @Override
    protected void updateFinished(float[][] evaluations) {
        
        //assign evaluations to the population as fitnesses		
        if (evaluations == null) {
            for (int i = 0; i < popSize; i++) {
                offspring.get(i).setFitnessValue(1);
            }
            return;
        }
        
        //initial generation -- evaluate the random population
        if (genNumber == 0) {
            for (int i = 0; i < evaluations.length; i++) {
                parents.get(i).setFitnessValue(evaluations[i][0]);
                ((SearchItemIndividual)parents.get(i)).setObjectives(evaluations[i]);
                if (evaluations[i][0] < bestError) {
                    bestError = evaluations[i][0];
                }
                archive.add((SearchItemIndividual)parents.get(i));
            }
            return;
        }
        
        for (int i = 0; i < evaluations.length; i++) {
            toEvaluate.get(i).setFitnessValue(evaluations[i][0]);
            ((SearchItemIndividual)toEvaluate.get(i)).setObjectives(evaluations[i]);
            if (evaluations[i][0] < bestError) {
                bestError = evaluations[i][0];
            }
            archive.add((SearchItemIndividual)toEvaluate.get(i));
        }
        
        offspring.clear();
        offspring.addAll(toEvaluate);
        offspring.addAll(evaluated);
        
        Population selected = new Population();

        ArrayList<Individual> sortedOld = parents.getSortedIndividuals();
        for (int i = 0; i < eliteSize*parents.getPopulationSize(); i++) {
            selected.add(sortedOld.get(i));
        }
        
        Population combined = replacement.replace(parents, offspring);
        
        if (multiobjective) {
            NSGAFitnessEvaluator NSGAfit = new NSGAFitnessEvaluator();
            NSGAfit.evaluate(combined);
        }
        
        int envSel = environmentalSelectors.size();
        int toSelect = (parents.getPopulationSize() - selected.getPopulationSize())/envSel;
        for (int i = 0; i < environmentalSelectors.size(); i++) {
            Population sel = new Population();
            environmentalSelectors.get(i).select(toSelect, combined, sel);
            selected.addAll((Population)sel.clone());
        }

        int missing = parents.getPopulationSize() - selected.getPopulationSize();
        if (missing > 0) {
            Population sel = new Population();
            environmentalSelectors.get(environmentalSelectors.size() - 1).select(toSelect, combined, sel);
            selected.addAll((Population)sel.clone());
        }

        parents.clear();
        parents.addAll(selected);
    }

    @Override
    protected void loadSearchOptions() {
    	
        popSize = 10;
        mutProb = 0.2;
        xOverProb = 0.5;
        maxGeneration = 5;
        goalError = 0.02;

        NewOptionList options = new NewOptionList(getSearchOptions());
        
        if (options.containsOptionWithName("E")) {
	        NewOption optionE = options.getOptionByName("E");
	        FloatValue valueE = (FloatValue) optionE.convertToSingleValue().getTypedValue();
	        goalError = valueE.getValue();
        }
        if (options.containsOptionWithName("M")) {
	        NewOption optionM = options.getOptionByName("M");
	        IntegerValue valueM = (IntegerValue) optionM.convertToSingleValue().getTypedValue();
	        maxGeneration = valueM.getValue();
        }
        if (options.containsOptionWithName("T")) {
	        NewOption optionT = options.getOptionByName("T");
	        FloatValue valueT = (FloatValue) optionT.convertToSingleValue().getTypedValue();
	        mutProb = valueT.getValue();
        }
        if (options.containsOptionWithName("X")) {
	        NewOption optionX = options.getOptionByName("X");
	        FloatValue valueX = (FloatValue) optionX.convertToSingleValue().getTypedValue();
	        xOverProb = valueX.getValue();
        }
        if (options.containsOptionWithName("P")) {
	        NewOption optionP = options.getOptionByName("P");
	        IntegerValue valueP = (IntegerValue) optionP.convertToSingleValue().getTypedValue();
	        popSize = valueP.getValue();
        }
        if (options.containsOptionWithName("I")) {
	        NewOption optionI = options.getOptionByName("I");
	        IntegerValue valueI = (IntegerValue) optionI.convertToSingleValue().getTypedValue();
	        maxEval = valueI.getValue();
        }
        if (options.containsOptionWithName("F")) {
	        NewOption optionF = options.getOptionByName("F");
	        FloatValue valueF = (FloatValue) optionF.convertToSingleValue().getTypedValue();
	        mutProbPerField = valueF.getValue();
        }

        if (options.containsOptionWithName("L")) {
	        NewOption optionL = options.getOptionByName("L");
	        FloatValue valueL = (FloatValue) optionL.convertToSingleValue().getTypedValue();
	        eliteSize = valueL.getValue();
        }

        //if (next.getName().equals("S")) {
        //    surrogate = Boolean.parseBoolean(next.getValue());
        //}
        //if (next.getName().equals("O")) {
        //    multiobjective = Boolean.parseBoolean(next.getValue());
        //}
        
        query_block_size = popSize;

    }

}