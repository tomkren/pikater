package cz.tomkren.typewars.dag;

import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.TypedDag;
import cz.tomkren.typewars.eva.*;
import cz.tomkren.typewars.eva.PolyTreeGenerator;
import cz.tomkren.typewars.reusable.QuerySolver;
import cz.tomkren.typewars.eva.SameSizeSubtreeMutation;
import cz.tomkren.typewars.SmartLib;

import java.util.Arrays;
import java.util.Random;

/** Created by tom on 30. 6. 2015.*/

public class DagEva01 {

    public static void main(String[] args) {

        // 3265394041486059844L - cenej seed na kerym to haže martinovi warning, z piety zatím nesmazáno
        Checker ch = new Checker();

        TogetherFitFun fitness = new DataScientistFitness("http://127.0.0.1:8080", "winequality-white.csv");
        //FitFun fitness = o -> {TypedDag dag = (TypedDag)o; return new FitVal.Basic( ((double)dag.getHeight()) / ((double)dag.getWidth()) ); };

        String goalType = "D => LD";
        SmartLib lib = SmartLib.DATA_SCIENTIST_01;
        int generatingMaxTreeSize = 35;


        Random rand = ch.getRandom();
        QuerySolver querySolver = new QuerySolver(lib, rand);

        EvoOpts evoOpts = new EvoOpts(10,8,true);
        //EvoOpts evoOpts = new EvoOpts(100,8,true);

        IndivGenerator<PolyTree> gen = new PolyTreeGenerator(goalType, generatingMaxTreeSize, querySolver);
        Distribution<Operator<PolyTree>> operators = new Distribution<>(Arrays.asList(
                new BasicTypedXover(rand, 0.5),
                new SameSizeSubtreeMutation(querySolver,generatingMaxTreeSize, 0.4),
                new CopyOp<>(0.1)
        ));

        //Selection<PolyTree> selection = new Selection.Roulette<>(rand);
        Selection<PolyTree> selection = new Selection.Tournament<>(0.8, rand);

        Logger<PolyTree> logger = new SimpleLogger();

        Evolver<PolyTree> evolver = new Evolver.Opts<>(fitness, evoOpts, gen, operators, selection, logger, rand).mk();

        Log.it("Generating initial population...");
        evolver.startRun();

        ch.results();
    }

    public static class SimpleLogger implements Logger<PolyTree> {

        @Override
        public void logPop(int run, int generation, EvaledPop<PolyTree> pop) {
            PolyTree best = pop.getBestIndividual();
            Log.it("gen" + generation + " \t best: ["+best.getProbability()+"] " +best);
            Log.it( pop );
        }



    }

}
