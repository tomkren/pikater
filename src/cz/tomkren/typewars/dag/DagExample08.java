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

        SmartLib lib = SmartLib.mk(
                "TypedDag.dia( TypedDag: D => D , TypedDag: D => (V LD n) , TypedDag: (V LD n) => LD ) : D => LD",
                "TypedDag.dia0( TypedDag: D => (V LD n) , TypedDag: (V LD n) => LD ) : D => LD",
                "TypedDag.split( TypedDag: D => (V D n) , MyList: V (D => LD) n ) : D => (V LD n)",

                "MyList.cons( Object: a , MyList: V a n ) : V a (S n)",
                "MyList.nil : V a 0",

                "PCA : D => D",
                "kBest : D => D",

                "kMeans : D => (V D (S(S n)))",
                "copy : D => (V D (S(S n)))",

                "SVC        : D => LD",
                "logR       : D => LD",
                "gaussianNB : D => LD",
                "DT         : D => LD",

                "vote : (V LD (S(S n))) => LD"
        );

        QuerySolver solver = new QuerySolver(lib, ch.getRandom());

        List<PolyTree> trees = solver.uniformGenerate("D => LD", 35, 1000);
        Log.list(trees);

        List<TypedDag> dags = F.map(trees, t->(TypedDag)t.computeValue());

        ch.results();
        KutilMain.showDags(dags);
    }

}
