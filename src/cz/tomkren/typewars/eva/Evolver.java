package cz.tomkren.typewars.eva;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Evolver<Indiv extends FitIndiv> implements PopulationSolver<Indiv> {

    private final Opts<Indiv> opts;

    public Evolver(Opts<Indiv> opts) {this.opts = opts;}

    public static class Opts<Ind extends FitIndiv> {
        private final IndivGenerator<Ind> generator;
        private final FitFun fitness;
        private final EvoOpts evoOpts;
        private final Random rand;
        private final Distribution<Operator<Ind>> operators;
        private final Logger<Ind> logger;

        public Opts(IndivGenerator<Ind> generator, FitFun fitness, EvoOpts evoOpts, Random rand, Distribution<Operator<Ind>> operators, Logger<Ind> logger) {
            this.generator = generator;
            this.fitness = fitness;
            this.evoOpts = evoOpts;
            this.rand = rand;
            this.operators = operators;
            this.logger = logger;
        }
    }

    @Override
    public List<Indiv> generatePop() {
        return opts.generator.generate(getPopSize());
    }

    @Override
    public EvaledPop<Indiv> evalPop(List<Indiv> pop, int gen) {
        return new EPop(pop, gen);
    }

    private class EPop implements EvaledPop<Indiv> {

        private Distribution<Indiv> popDist;
        private List<Indiv> terminators;

        public EPop(List<Indiv> pop, int gen) {
            FitFun fitness = opts.fitness;
            fitness.initGeneration(gen);
            terminators = new ArrayList<>();
            for (Indiv ind : pop) {
                FitVal fitVal = ind.evaluate(fitness);
                if (fitVal.isOK()) {
                    terminators.add( ind );
                }
            }
            popDist = new Distribution<>(pop);
        }

        @Override public boolean isTerminating() {return !terminators.isEmpty();}
        @Override public Indiv getBestIndividual() {return popDist.getBest();}
        @Override public Distribution<Indiv> getIndividuals() {return popDist;}
        @Override public List<Indiv> getTerminators() {return terminators;}
    }

    @Override public int     getNumRuns() {return opts.evoOpts.getNumRuns();}
    @Override public int     getNumGens() {return opts.evoOpts.getNumGens();}
    @Override public int     getPopSize() {return opts.evoOpts.getPopSize();}
    @Override public boolean saveBest()   {return opts.evoOpts.isSaveBest();}

    @Override public Random getRandom() {return opts.rand;}
    @Override public Distribution<Operator<Indiv>> getOperators() {return opts.operators;}
    @Override public Logger<Indiv> getLogger() {return opts.logger;}

}
