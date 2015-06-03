package cz.tomkren.typewars.dag;

import cz.tomkren.helpers.Checker;
import cz.tomkren.typewars.CodeLib;

public class DagExample3 {

    public static void main(String[] args) {
        Checker ch = new Checker();

        CodeLib.mk(
            "TypedDag.dia( TypedDag: a => a , TypedDag: a => (V b n) , TypedDag: (V b n) => b ) : a => b",
            "TypedDag.split( TypedDag: a => (V a n) , MyList: V (a => b) n ) : a => (V b n)",
            "MyList.cons( Object: a , MyList: V a n ) : V a (S n)",
            "MyList.nil : V a 0",
            "PCA : D => D",
            "k-means : D => (V D (S(S n)))",
            "MLP : D => LD",
            "U : (V LD (S(S n))) => LD"
        ).generate("D => LD", 10).forEach(tree ->
            ch.itln("...").itln(tree).itln(tree.showWithTypes()).it(tree.computeValue())
        );

        ch.results();
    }



}
