package cz.tomkren.typewars.eva;


import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.SmartLib;
import cz.tomkren.typewars.Type;
import cz.tomkren.typewars.Types;
import cz.tomkren.typewars.dag.DataScientistFitness;
import cz.tomkren.typewars.reusable.QuerySolver;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

/** Created by tom on 6.7.2015. */

public class DagEva02 {

    public static void main(String[] args) {
        Checker ch = new Checker();
        Random rand = ch.getRandom();

        DataScientistFitness fitness = new DataScientistFitness("http://127.0.0.1:8080", "wilt.csv"); // "winequality-white.csv";
        SmartLib lib = SmartLib.mkDataScientistLib01FromParamsInfo(fitness.getAllParamsInfo());
        QuerySolver querySolver = new QuerySolver(lib, rand);
        Type goalType = Types.parse("D => LD");

        int numGenerations = 10;
        int populationSize = 8;
        boolean saveBest = true;

        int generatingMaxTreeSize = 35;
        int mutationMaxSubtreeSize = 20;

        IndivGenerator<PolyTree> generator = new RandomParamsPolyTreeGenerator(goalType, generatingMaxTreeSize, querySolver);
        Selection<PolyTree> selection = new Selection.Tournament<>(0.8, rand);
        Distribution<Operator<PolyTree>> operators = new Distribution<>(Arrays.asList(
                new BasicTypedXover(rand, 0.5),
                new SameSizeSubtreeMutation(querySolver, mutationMaxSubtreeSize, 0.4),
                new CopyOp<>(0.1)
        ));

        Evolver<PolyTree> evolver = new Evolver.Opts<>(fitness, new EvoOpts(numGenerations,populationSize,saveBest), generator, operators, selection, new PolyTreeEvolutionLogger(), rand).mk();

        Log.it("Generating initial population...");
        evolver.startRun();

        ch.results();
    }



}
