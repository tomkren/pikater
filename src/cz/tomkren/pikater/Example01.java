package cz.tomkren.pikater;

import cz.tomkren.helpers.Checker;

import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.CodeLib;
import cz.tomkren.typewars.TypedDag;
import org.pikater.core.agents.experiment.computing.Agent_WekaMultilayerPerceptronCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_CCIPercentage;
import org.pikater.core.experiments.Input_Tom1;
import org.pikater.core.experiments.Rucni;
import org.pikater.core.experiments.Rucni_simple;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;

import java.util.List;
import java.util.function.Function;

/** Created by tom on 1.6.2015.*/

public class Example01 {

    public static class BoxUtils {

        public static DataSourceDescription mkOutput(Integer i, DataProcessing dataProcessing, Function<Integer,String> show) {
            DataSourceDescription output = new DataSourceDescription();
            output.setOutputType(show.apply(i));
            output.setDataProvider(dataProcessing);
            return output;
        }

        public static DataSourceDescription mkOutput0(Integer i, DataProcessing dataProcessing) {
            if (i != 0) {throw new Error("Expected only one output.");}
            return mkOutput(i, dataProcessing, j -> "Data");
        }

        public static DataSourceDescription err_mkOutput(Integer i, DataProcessing dataProcessing) {
            if (i != 0) {throw new Error("Expected only one output.");}
            return mkOutput(i, dataProcessing, j -> "Error");
        }

        public static DataSourceDescription mkOutput_i(Integer i, DataProcessing dataProcessing) {
            return mkOutput(i, dataProcessing, j -> "Data_"+j);
        }

        public static DataProcessing ca_MkDataProcessing(String agentType) {
            ComputingAgent ca = new ComputingAgent();
            ca.setAgentType(agentType);
            return ca;
        }

        public static void ca_setSources(DataProcessing dataProcessing, List<DataSourceDescription> sources) {
            DataSourceDescription source = sources.get(0);
            ComputingAgent ca = (ComputingAgent) dataProcessing;
            ca.setTrainingData(source);
            ca.setTestingData(source);
            ca.setValidationData(source);
            ca.setDataToLabel(source);
        }

        public static void err_setSources(DataProcessing err, List<DataSourceDescription> sources) {
            DataSourceDescription labeledDataSource = sources.get(0);
            DataSourceDescription fileDataSource = sources.get(1);

            err.addDataSources(labeledDataSource);
            err.addDataSources(fileDataSource);
        }

        public static void setSources_binar(DataProcessing dataProcessing, List<DataSourceDescription> sources) {
            dataProcessing.addDataSources(sources.get(0));
            dataProcessing.addDataSources(sources.get(1));
        }


    }

    public static void main(String[] args) {
        Checker ch = new Checker();



        // TODO napojit na zbytek se musí


        // Jednotlivý krabièky

        BoxPrototype input = new InputPrototype("weather.arff");

        BoxPrototype err = new BoxPrototype("err", Agent_CCIPercentage.class, BoxPrototype::mkDataProcessing_default, BoxUtils::err_setSources, BoxUtils::err_mkOutput);

        BoxPrototype pca    = new BoxPrototype("PCA", /*TODO Agent_PCA.class*/ Agent_WekaRBFNetworkCA.class, BoxUtils::mkOutput0);
        BoxPrototype kmeans = new BoxPrototype("k-means", /*TODO Agent_KMeans.class*/ Agent_WekaRBFNetworkCA.class, BoxUtils::mkOutput_i);
        BoxPrototype rbf    = new BoxPrototype("RBF", Agent_WekaRBFNetworkCA.class, BoxUtils::ca_MkDataProcessing, BoxUtils::ca_setSources, BoxUtils::mkOutput0);
        BoxPrototype mlp    = new BoxPrototype("MLP", Agent_WekaMultilayerPerceptronCA.class, BoxUtils::ca_MkDataProcessing, BoxUtils::ca_setSources, BoxUtils::mkOutput0);
        BoxPrototype u      = new BoxPrototype("U", /*TODO Agent_RomanovoU.class*/ Agent_WekaRBFNetworkCA.class, BoxPrototype::mkDataProcessing_default, BoxUtils::setSources_binar, BoxUtils::mkOutput0);

        BoxPrototype output = new BoxPrototype("output", null, at -> new FileDataSaver(), (dp,sources)-> ((FileDataSaver)dp).setDataSource(sources.get(0)), (i,dp)-> null);



        // Vytvoøíme konvertor nadiktováním knihovny krabièek
        Converter converter = new Converter(input, pca, kmeans, rbf, mlp, u, err, output);


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

            CodeLib.mk(
                    "TypedDag.dia( TypedDag: a => a , TypedDag: a => (V b n) , TypedDag: (V b n) => b ) : a => b",
                    "TypedDag.split( TypedDag: a => (V a n) , MyList: V (a => b) n ) : a => (V b n)",
                    "MyList.cons( Object: a , MyList: V a n ) : V a (S n)",
                    "MyList.nil : V a 0",
                    "PCA : D => D",
                    "k-means : D => (V D (S(S n)))",
                    "RBF : D => LD",
                    "U : (V LD (S(S n))) => LD"
            ).generate("D => LD", 1).forEach(tree -> {

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
