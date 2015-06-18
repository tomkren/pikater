package cz.tomkren.typewars.dag;

import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.F;
import cz.tomkren.kutil2.KutilMain;
import cz.tomkren.typewars.CodeLib;
import cz.tomkren.typewars.TypedDag;

/** Created by tom on 18. 6. 2015. */

public class DagExample4 {

    public static void main(String[] args) {
        Checker ch = new Checker();

        String goalType = "D => LD";
        int numTrees = 10;
        String[] libStrs = {
            "TypedDag.dia( TypedDag: a => a , TypedDag: a => (V b n) , TypedDag: (V b n) => b ) : a => b",
            "TypedDag.split( TypedDag: a => (V a n) , MyList: V (a => b) n ) : a => (V b n)",
            "MyList.cons( Object: a , MyList: V a n ) : V a (S n)",
            "MyList.nil : V a 0",
            "PCA : D => D",
            "k-means : D => (V D (S(S n)))",
            "MLP : D => LD",
            "U : (V LD (S(S n))) => LD"
        };

        CodeLib codeLib1 = CodeLib.mk(libStrs);

        String popJson = mkPopJson(codeLib1, goalType, numTrees);
        ch.it(popJson);

        F.writeFile("population.json", popJson);

        ch.results();

        KutilMain.starLib(libStrs,goalType,numTrees);
    }

    public static String mkPopJson(CodeLib lib, String type, int n) {
        return F.list(lib.generate(type,n)).map(t->(TypedDag)t.computeValue()).reduce(TypedDag::toJson);
    }

}
