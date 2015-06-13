package cz.tomkren.typewars.dag;

import cz.tomkren.helpers.Checker;
import cz.tomkren.typewars.CodeLib;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.reusable.Fun;
import cz.tomkren.typewars.reusable.SmartLib;
import cz.tomkren.typewars.reusable.TMap;

public class DagExample3 {

    public static void main(String[] args) {
        Checker ch = new Checker();

        CodeLib codeLib = CodeLib.mk(
            "TypedDag.dia( TypedDag: a => a , TypedDag: a => (V b n) , TypedDag: (V b n) => b ) : a => b",
            "TypedDag.split( TypedDag: a => (V a n) , MyList: V (a => b) n ) : a => (V b n)",
            "MyList.cons( Object: a , MyList: V a n ) : V a (S n)",
            "MyList.nil : V a 0",
            "PCA : D => D",
            "k-means : D => (V D (S(S n)))",
            "MLP : D => LD",
            "U : (V LD (S(S n))) => LD"
        );

        codeLib.generate("D => LD", 10).forEach(tree ->
            ch.it(tree)
            //ch.itln("...").itln(tree).itln(tree.showWithTypes()).it(tree.computeValue())
        );

        ch.it("\n");

        SmartLib lib = new SmartLib(codeLib);


        int num = 9;
        for (int n = 1; n <= num; n++) {
            checkSize(n, lib, ch);
        }

        // TODO : tady тбkб chyba !!!!!!!!!!!
        checkSize(10, lib, ch);


        ch.results();
    }

    public static void checkSize(int n, SmartLib lib, Checker ch) {
        TMap<PolyTree> tMap = Fun.generateAll(lib, "D => LD", n);
        ch.it("("+n+") ... tMap.size = "+tMap.size());
        ch.it(tMap);
    }



}
