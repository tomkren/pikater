package cz.tomkren.pikater;

import cz.tomkren.helpers.Checker;

import cz.tomkren.helpers.Log;
import org.pikater.core.agents.experiment.computing.Agent_WekaMultilayerPerceptronCA;
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
            // TODO ca.setDataToLabel(dataSourceKMeans1);
        }

        public static void setSources_binar(DataProcessing dataProcessing, List<DataSourceDescription> sources) {
            dataProcessing.addDataSources(sources.get(0));
            dataProcessing.addDataSources(sources.get(1));
        }


    }

    public static void main(String[] args) {
        Checker ch = new Checker();



        // TODO napojit na zbytek se mus�


        // Jednotliv� krabi�ky

        BoxPrototype input = new BoxPrototype("input", null, at->null, (dp,sources)->{}, (i,dp)-> {
            FileDataProvider fileDataProvider = new FileDataProvider();
            fileDataProvider.setFileURI("weather.arff");
            DataSourceDescription fileDataSource = new DataSourceDescription();
            fileDataSource.setDataProvider(fileDataProvider);
            return fileDataSource;
        });

        BoxPrototype pca    = new BoxPrototype("PCA", /*TODO Agent_PCA.class*/ Agent_WekaRBFNetworkCA.class, BoxUtils::mkOutput0);
        BoxPrototype kmeans = new BoxPrototype("k-means", /*TODO Agent_KMeans.class*/ Agent_WekaRBFNetworkCA.class, BoxUtils::mkOutput_i);
        BoxPrototype rbf    = new BoxPrototype("RBF", Agent_WekaRBFNetworkCA.class, BoxUtils::ca_MkDataProcessing, BoxUtils::ca_setSources, BoxUtils::mkOutput0);
        BoxPrototype mlp    = new BoxPrototype("MLP", Agent_WekaMultilayerPerceptronCA.class, BoxUtils::ca_MkDataProcessing, BoxUtils::ca_setSources, BoxUtils::mkOutput0);
        BoxPrototype u      = new BoxPrototype("U", /*TODO Agent_RomanovoU.class*/ Agent_WekaRBFNetworkCA.class, BoxPrototype::mkDataProcessing_default, BoxUtils::setSources_binar, BoxUtils::mkOutput0);

        BoxPrototype output = new BoxPrototype("output", null, at -> new FileDataSaver(), (dp,sources)-> ((FileDataSaver)dp).setDataSource(sources.get(0)), (i,dp)-> null);



        // Vytvo��me konvertor nadiktov�n�m knihovny krabi�ek
        Converter converter = new Converter(input, pca, kmeans, rbf, mlp, u, output);

        // Pop�em gr�fek
        List<SimpleVertex> graph = SimpleVertex.readLines(
            "0 input 0 1 1:0",
            "1 PCA 1 1 2:0",
            "2 k-means 1 2 3:0 4:0",
            "3 RBF 1 1 5:0",
            "4 MLP 1 1 5:1",
            "5 U 2 1 6:0",
            "6 output 1 0"
        );

        Log.list(graph);

        try {


            ComputationDescription cd = converter.convert(graph);

            Log.it(cd.exportXML());



        } catch (Converter.ConverterError converterError) {
            converterError.printStackTrace();
        }


        ch.results();
    }

}
