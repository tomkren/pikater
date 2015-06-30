package cz.tomkren.typewars.dag;

import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.TypedDag;
import cz.tomkren.typewars.eva.*;
import cz.tomkren.typewars.reusable.PolyTreeGenerator;
import cz.tomkren.typewars.reusable.QuerySolver;
import cz.tomkren.typewars.reusable.SameSizeSubtreeMutation;
import cz.tomkren.typewars.reusable.SmartLib;

import java.util.Arrays;
import java.util.Random;

/** Created by tom on 30. 6. 2015.*/

public class DagEva01 {

    public static void main(String[] args) {
        Checker ch = new Checker();
        Random rand = ch.getRandom();


        String goalType = "D => LD";
        SmartLib lib = SmartLib.EXAMPLE01;
        int generatingMaxTreeSize = 35;
        FitFun fitness = o -> new FitVal.Basic(((TypedDag)o).getHeight());

        QuerySolver querySolver = new QuerySolver(lib, rand);
        EvoOpts evoOpts = new EvoOpts(1,51,500,true);
        IndivGenerator<PolyTree> gen = new PolyTreeGenerator(goalType, generatingMaxTreeSize, querySolver);
        Distribution<Operator<PolyTree>> operators = new Distribution<>(Arrays.asList(
                new SameSizeSubtreeMutation(querySolver, 0.9),
                new CopyOp<>(0.1)
        ));

        Logger<PolyTree> logger = new SimpleLogger();

        Evolver<PolyTree> evolver = new Evolver.Opts<>(gen, fitness, evoOpts, rand, operators, logger).mk();

        evolver.startRun();

        ch.results();
    }

    public static class SimpleLogger implements Logger<PolyTree> {

        @Override
        public void logPop(int run, int generation, EvaledPop<PolyTree> pop) {
            PolyTree best = pop.getBestIndividual();
            Log.it("gen" + generation + " \t best: ["+best.getProbability()+"] " +best);
        }



    }

}
