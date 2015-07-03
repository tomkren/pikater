package cz.tomkren.pikater.tests;

import cz.tomkren.helpers.Checker;
import cz.tomkren.pikater.BoxUtils;
import cz.tomkren.pikater.Converter;
import cz.tomkren.pikater.SimpleVertex;
import cz.tomkren.typewars.CodeLib;
import cz.tomkren.typewars.TypedDag;
import org.pikater.core.experiments.Input_Tom1;
import org.pikater.core.experiments.Rucni;
import org.pikater.core.experiments.Rucni_simple;

import java.util.List;

/** Created by tom on 1.6.2015.*/

public class Tests01 {

    public static void main(String[] args) {
        Checker ch = new Checker();

        BoxUtils boxUtils = new BoxUtils("weather.arff");


        Converter converter = boxUtils.getConverter();

        try {

            List<SimpleVertex> graph_simple = SimpleVertex.readLines(
                "0 input  0 2 1:0 1:1",
                "1 err    2 1 2:0",
                "2 output 1 0"
            );

            ch.eqStrSilent(converter.convert(graph_simple).exportXML(), new Rucni_simple().createDescription().exportXML());

            List<SimpleVertex> graph_goal = SimpleVertex.readLines(
                "0 input    0 2 1:0 6:1",
                "1 PCA      1 1 2:0",
                "2 k-means  1 2 3:0 4:0",
                "3 RBF      1 1 5:0",
                "4 MLP      1 1 5:1",
                "5 U        2 1 6:0",
                "6 err      2 1 7:0",
                "7 output   1 0"
            );

            ch.eqStrSilent(converter.convert(graph_goal).exportXML(), new Rucni().createDescription().exportXML());

            ch.eqStrSilent(
                    new Net_PCA_RBF_gen().createDescription().exportXML(),
                    new Net_PCA_RBF_onto().createDescription().exportXML()
            );


            CodeLib.mk(
                    "TypedDag.dia( TypedDag: a => a , TypedDag: a => (V b n) , TypedDag: (V b n) => b ) : a => b",
                    "TypedDag.split( TypedDag: a => (V a n) , MyList: V (a => b) n ) : a => (V b n)",
                    "MyList.cons( Object: a , MyList: V a n ) : V a (S n)",
                    "MyList.nil : V a 0",
                    "PCA : D => D",
                    "k-means : D => (V D (S(S n)))",
                    "RBF : D => LD",
                    "U : (V LD (S(S n))) => LD"
            ).basicGenerate("D => LD", 1).forEach(tree -> {

                TypedDag dag = (TypedDag) tree.computeValue();

                ch.itln("...").itln(tree).itln(tree.showWithTypes()).it(dag);

                ch.list(dag.toSimpleGraph()).ln();

                try {

                    ch.eqStrSilent(
                            converter.convert(dag).exportXML(),
                            new Input_Tom1().createDescription().exportXML()
                    );

                } catch (Converter.ConverterError converterError) {
                    converterError.printStackTrace();
                }


            });



        } catch (Converter.ConverterError converterError) {
            converterError.printStackTrace();
        }


        ch.results();
    }

}
