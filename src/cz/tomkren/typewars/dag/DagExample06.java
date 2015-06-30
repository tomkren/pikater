package cz.tomkren.typewars.dag;

import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.F;
import cz.tomkren.kutil2.KutilMain;
import cz.tomkren.typewars.CodeLib;
import cz.tomkren.typewars.TypedDag;

import java.util.List;

/** Created by tom on 18. 6. 2015. */

public class DagExample06 {

    public static void main(String[] args) {
        Checker ch = new Checker();

        String goalType = "D => LD";
        int numTrees = 18000; //65536; //500;
        String[] libStrs = {
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
        };


        CodeLib codeLib1 = CodeLib.mk(libStrs);


        List<TypedDag> dags = mkDags(codeLib1, goalType, numTrees);
        String popJson = mkPopJson(dags);
        ch.it(popJson);

        F.writeFile("population_" + numTrees + ".json", popJson);

        int winnerId = 478 ; //17572;

        ch.results();

        KutilMain.showDag(dags.get(winnerId));

        //KutilMain.startLib(libStrs,goalType,numTrees);
    }

    public static String mkPopJson(List<TypedDag> dags) {
        return F.list(dags).reduce(TypedDag::toJson);
    }

    public static List<TypedDag> mkDags(CodeLib lib, String type, int n) {
        return F.list(lib.generate(type,n)).map(tree ->(TypedDag) tree.computeValue()).get();
    }

}
