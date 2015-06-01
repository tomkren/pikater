package cz.tomkren.pikater;

import cz.tomkren.helpers.Checker;

import cz.tomkren.helpers.Log;
import org.pikater.core.agents.experiment.computing.Agent_WekaMultilayerPerceptronCA;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

        public static DataSourceDescription mkOutput_i(Integer i, DataProcessing dataProcessing) {
            return mkOutput(i, dataProcessing, j -> "Data_"+j);
        }

        public static DataProcessing ca_MkDataProcessing(String agentType) {
            ComputingAgent ca = new ComputingAgent();
            ca.setAgentType(agentType);
            return ca;
        }

        public static void ca_setSources(DataProcessing dataProcessing, List<DataSourceDescription> sources) {
            ComputingAgent ca = (ComputingAgent) dataProcessing;
            DataSourceDescription source = sources.get(0);

            ca.setTrainingData(source);
            ca.setTestingData(source);
            ca.setValidationData(source);
            // TODO ca.setDataToLabel(dataSourceKMeans1);
        }


    }

    public static void main(String[] args) {
        Checker ch = new Checker();

        // Datasource
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI("weather.arff");
        DataSourceDescription fileDataSource = new DataSourceDescription();
        fileDataSource.setDataProvider(fileDataProvider);

        // TODO napojit na zbytek se musí


        // Jednotlivý krabièky
        BoxPrototype pca    = new BoxPrototype("PCA", /*TODO Agent_PCA.class*/ Agent_WekaRBFNetworkCA.class, BoxUtils::mkOutput0);
        BoxPrototype kmeans = new BoxPrototype("k-means", /*TODO Agent_KMeans.class*/ Agent_WekaRBFNetworkCA.class, BoxUtils::mkOutput_i);
        BoxPrototype rbf    = new BoxPrototype("RBF", Agent_WekaRBFNetworkCA.class, BoxUtils::ca_MkDataProcessing, BoxUtils::ca_setSources, BoxUtils::mkOutput0);
        BoxPrototype mlp    = new BoxPrototype("MLP", Agent_WekaMultilayerPerceptronCA.class, BoxUtils::ca_MkDataProcessing, BoxUtils::ca_setSources, BoxUtils::mkOutput0);



        // Vytvoøíme konvertor nadiktováním knihovny krabièek
        Converter converter = new Converter(pca, kmeans, rbf, mlp);

        // Popíšem gráfek
        List<SimpleVertex> graph = SimpleVertex.readLines(
            "0 PCA 1 1 1:0",
            "1 k-means 1 2 2:0 3:0",
            "2 RBF 1 1 4:0",
            "3 MLP 1 1 4:1"
            //"4 U 2 1"
        );

        Log.list(graph);

        try {


            converter.convert(graph);


        } catch (Converter.ConverterError converterError) {
            converterError.printStackTrace();
        }


        ch.results();
    }

}
