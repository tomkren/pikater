package cz.tomkren.typewars.dag;

import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.CodeLib;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.reusable.Fun;
import cz.tomkren.typewars.reusable.SmartLib;
import cz.tomkren.typewars.reusable.TMap;

public class DagExample3 {

    static Checker ch = new Checker();

    static CodeLib codeLib = CodeLib.mk(
            "TypedDag.dia( TypedDag: a => a , TypedDag: a => (V b n) , TypedDag: (V b n) => b ) : a => b",
            "TypedDag.split( TypedDag: a => (V a n) , MyList: V (a => b) n ) : a => (V b n)",
            "MyList.cons( Object: a , MyList: V a n ) : V a (S n)",
            "MyList.nil : V a 0",
            "PCA : D => D",
            "k-means : D => (V D (S(S n)))",
            "MLP : D => LD",
            "U : (V LD (S(S n))) => LD"
    );

    static SmartLib lib = new SmartLib(codeLib);


    public static void main(String[] args) {


        codeLib.generate("D => LD", 10).forEach(tree ->
            ch.it(tree)
            //ch.itln("...").itln(tree).itln(tree.showWithTypes()).it(tree.computeValue())
        );

        ch.it("\n");



        int num = 9;
        for (int n = 1; n <= num; n++) {
            checkQuery(n);
        }

        checkQuery(10); // TODO : tady òáká chyba, mìlo by dát 1 strom !!!!!!!!!!!

        Log.it("\n----------------------------------------\n");

        checkQuery("D => LD", 1); // OK
        checkQuery("V a 0", 1);   // OK
        checkQuery("V (D => LD) (S 0)", 3); // OK
        checkQuery("V (D => LD) (S (S 0))", 5); // OK
        checkQuery("D => (V LD (S (S 0)))", 7); // OK

        checkQuery("D => D", 1); // OK
        checkQuery("(V LD (S (S 0))) => LD", 1); // OK

        checkQuery("D => LD", 10); // todo tadfy se to teda rozbije



        ch.results();
    }

    public static void checkQuery(String type, int n) {
        TMap<PolyTree> tMap = Fun.generateAll(lib, type, n);
        ch.it("("+type+" ; "+n+") ... tMap.size = "+tMap.size());
        ch.it(tMap);
    }

    public static void checkQuery(int n) {
        checkQuery("D => LD", n);
    }



}
