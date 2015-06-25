package cz.tomkren.typewars.dag;

import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.CodeLib;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.Type;
import cz.tomkren.typewars.reusable.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Created by tom on 18.6.2015. */

public class DagExample07 {

    static Checker ch = new Checker();

    static CodeLib codeLib_pokus1 = CodeLib.mk(
            "TypedDag.dia( TypedDag: a => a , TypedDag: a => (V b n) , TypedDag: (V b n) => b ) : a => b",
            //"TypedDag.dia0( TypedDag: a => (V b n) , TypedDag: (V b n) => b ) : a => b",
            "TypedDag.split( TypedDag: a => (V a n) , MyList: V (a => b) n ) : a => (V b n)",
            "MyList.cons( Object: a , MyList: V a n ) : V a (S n)",
            "MyList.nil : V a 0",
            "PCA : D => D",
            "k-means : D => (V D (S(S n)))",
            "MLP : D => LD",
            "U : (V LD (S(S n))) => LD"
    );

    static CodeLib codeLib_pokus2 = CodeLib.mk(
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

    static CodeLib codeLib_pokus = codeLib_pokus2;

    static SmartLib lib = new SmartLib(codeLib_pokus); //(codeLib);

    static QuerySolver solver = new QuerySolver(lib);

    public static void main(String[] args) {

        //checkQuery("D => LD", 1, 1);
        //checkQuery("V a 0", 1, 1);
        //checkQuery("V (D => LD) (S 0)", 3, 1);
        //checkQuery("V (D => LD) (S (S 0))", 5, 1);
        //checkQuery("D => (V LD (S (S 0)))", 7, 1);
        //checkQuery("D => D", 1, 1);
        //checkQuery("(V LD (S (S 0))) => LD", 1, 1);
        //checkQuery("D => LD", 10, 1);
        //checkQuery("D => LD", 12, 1);

        String goalType = "D => LD";

        List<PolyTree> oldMethodTrees = codeLib_pokus.generate(goalType, 10000);  //40000 65536;

        GeneratorChecker genCheck = new GeneratorChecker(oldMethodTrees, false);

        List<Integer> nums_oldMethod = genCheck.getNumsForSizes();
        int upToTreeSize = nums_oldMethod.size();

        List<Integer> nums_newMethod = new ArrayList<>();

        TMap<PolyTree> newMethodTrees = solver.generateAllUpTo(goalType, upToTreeSize); //25

        genCheck.check(newMethodTrees, true);

        for (int treeSize = 1; treeSize <= upToTreeSize; treeSize++) {
            int num = solver.query(goalType, treeSize).getNum().intValue();
            nums_newMethod.add(num);
            Log.it(treeSize+"/"+upToTreeSize+": "+num +" ["+(num == nums_oldMethod.get(treeSize-1) ? "OK":"KO!!!!!!")+"]"
            +"  reuse-ratio: "+solver.getReuseRatio());
        }

        Log.it("nums [sizes: 1..."+upToTreeSize+"] = \n"+ nums_oldMethod );
        Log.it(nums_newMethod);

        Log.it( "reuse ratio: "+solver.getReuseRatio() );


        //QueryResult qRes = solver.query("D => LD", 1);
        //ch.it("qRes.getNum() = " + qRes.getNum());

        ch.results();
    }

    public static void checkQuery(String type, int n) {
        checkQuery(type, n, null);
    }

    public static void checkQuery(String type, int n, Integer expectedNumTrees) {
        TMap<PolyTree> tMap = Fun2.generateAll(lib, type, n);

        QueryResult qr = solver.query(type, n);

        ch.it("(" + type + " ; " + n + ") ... tMap.size = " + tMap.size()+" ... qr.getNum() = "+qr.getNum());
        ch.it(tMap);

        if (expectedNumTrees != null) {
            ch.it(tMap.size(), expectedNumTrees);
            ch.it(qr.getNum(), expectedNumTrees.toString());
            Log.it();
        }
    }

}
