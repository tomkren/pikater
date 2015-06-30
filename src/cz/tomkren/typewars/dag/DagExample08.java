package cz.tomkren.typewars.dag;

import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.F;
import cz.tomkren.helpers.Log;
import cz.tomkren.kutil2.KutilMain;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.TypedDag;
import cz.tomkren.typewars.reusable.QuerySolver;
import cz.tomkren.typewars.reusable.SmartLib;

import java.util.List;

/** Created by tom on 25.6.2015. */

public class DagExample08 {

    public static void main(String[] args) {
        Checker ch = new Checker();

        SmartLib lib = SmartLib.EXAMPLE01;

        QuerySolver solver = new QuerySolver(lib, ch.getRandom());

        List<PolyTree> trees = solver.uniformGenerate("D => LD", 35, 1000);
        Log.list(trees);

        List<TypedDag> dags = F.map(trees, t->(TypedDag)t.computeValue());

        ch.results();
        KutilMain.showDags(dags);
    }

}
