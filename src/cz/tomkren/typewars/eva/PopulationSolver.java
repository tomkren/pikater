package cz.tomkren.typewars.eva;

import cz.tomkren.helpers.F;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface PopulationSolver<Indiv extends Probable> {

    Random getRandom();
    int getNumRuns();
    int getNumGens();
    int getPopSize();
    List<Indiv> generatePop();
    EvaledPop<Indiv> evalPop(List<Indiv> pop, int gen);
    boolean saveBest();
    Distribution<Operator<Indiv>> getOperators();
    Logger<Indiv> getLogger();


    default void startRun(int run) {

        Random rand = getRandom();

        int numGens = getNumGens();
        int popSize = getPopSize();
        Distribution<Operator<Indiv>> operators = getOperators();
        Logger<Indiv> logger = getLogger();

        int gen = 0;
        List<Indiv> pop = generatePop();
        EvaledPop<Indiv> evaledPop = evalPop(pop,gen);
        logger.logPop(run, gen, evaledPop);

        while (gen < numGens-1 && !evaledPop.isTerminating()) {
            pop = new ArrayList<>(popSize);

            // TODO generalize to elitism
            if (saveBest()) {
                pop.add( evaledPop.getBestIndividual() );
            }

            // fill the new pop
            while (pop.size() < popSize) {

                Operator<Indiv> operator = operators.get(rand);

                int numParents = operator.getNumInputs();
                List<Indiv> parents = new ArrayList<>(numParents);

                Distribution<Indiv> popDistrib = evaledPop.getIndividuals();
                for (int i=0; i<numParents; i++) {
                    parents.add(popDistrib.get(rand));
                }

                List<Indiv> operatorChildren = operator.operate(parents);
                int maxNumChildren = popSize - pop.size();
                List<Indiv> children = F.take(maxNumChildren, operatorChildren);

                pop.addAll(children);
            }

            gen ++;
            evaledPop = evalPop(pop, gen);
            logger.logPop(run, gen, evaledPop);
        }

        logger.logRun(run);
    }

    default void startRun() {startRun(1);}

    default void startRuns() {
        int numRuns = getNumRuns();
        for (int run=1; run<=numRuns; run++) {
            startRun(run);
        }
    }


}
