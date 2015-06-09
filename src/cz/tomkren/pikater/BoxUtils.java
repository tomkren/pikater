package cz.tomkren.pikater;

import org.pikater.core.agents.experiment.computing.Agent_WekaMultilayerPerceptronCA;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_KMeans;
import org.pikater.core.agents.experiment.dataprocessing.Agent_PCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_RomanovoU;
import org.pikater.core.agents.experiment.dataprocessing.errorcomputing.Agent_Accuracy;
import org.pikater.core.ontology.subtrees.batchdescription.*;

import java.util.List;
import java.util.function.Function;

// TODO udìlat to nestatický !!!!!!!!!!!!!!!!!!!!


/** Created by tom on 5. 6. 2015. */
public class BoxUtils {



    private final Converter converter;


    public BoxUtils(String filename) {

        // Jednotlivý krabièky

        BoxPrototype input = new InputPrototype("weather.arff");


        BoxPrototype pca    = new BoxPrototype("PCA", Agent_PCA.class, BoxUtils::mkOutput0);
        BoxPrototype kmeans = new BoxPrototype("k-means", Agent_KMeans.class, BoxUtils::mkOutput_i);
        BoxPrototype rbf    = new BoxPrototype("RBF", Agent_WekaRBFNetworkCA.class, BoxUtils::ca_MkDataProcessing, BoxUtils::ca_setSources, BoxUtils::mkOutput0);
        BoxPrototype mlp    = new BoxPrototype("MLP", Agent_WekaMultilayerPerceptronCA.class, BoxUtils::ca_MkDataProcessing, BoxUtils::ca_setSources, BoxUtils::mkOutput0);
        BoxPrototype u      = new BoxPrototype("U",   Agent_RomanovoU.class, BoxPrototype::mkDataProcessing_default, BoxUtils::setSources_binar, BoxUtils::mkOutput0);

        BoxPrototype err = new BoxPrototype("err", Agent_Accuracy.class, BoxPrototype::mkDataProcessing_default, BoxUtils::err_setSources, BoxUtils::err_mkOutput);
        BoxPrototype output = new BoxPrototype("output", null, at -> new FileDataSaver(), (dp,sources)-> ((FileDataSaver)dp).setDataSource(sources.get(0)), (i,dp)-> null);

        // Vytvoøíme konvertor nadiktováním knihovny krabièek
        converter = new Converter(input, pca, kmeans, rbf, mlp, u, err, output);
    }

    public Converter getConverter() {
        return converter;
    }

    public static ComputationDescription mkComputationDescription(String filename, String... graphLines) {
        BoxUtils bu = new BoxUtils(filename);

        return bu.mkComputationDescription_(graphLines);
    }


    private ComputationDescription mkComputationDescription_(String... graphLines) {
        try {
            return getConverter().convert(graphLines);
        } catch (Converter.ConverterError converterError) {
            converterError.printStackTrace();
            return null;
        }
    }


    private ComputationDescription mkComputationDescription_(ComputationDescription ifThisNull, String... graphLines) {
        if (ifThisNull == null) {
            try {
                return getConverter().convert(graphLines);
            } catch (Converter.ConverterError converterError) {
                converterError.printStackTrace();
                return null;
            }
        }
        return ifThisNull;
    }

    public static DataSourceDescription mkOutput(Integer i, DataProcessing dataProcessing, Function<Integer, String> show) {
        DataSourceDescription output = new DataSourceDescription();
        output.setOutputType(show.apply(i));
        output.setDataProvider(dataProcessing);
        return output;
    }

    public static DataSourceDescription mkOutput0(Integer i, DataProcessing dataProcessing) {
        if (i != 0) {
            throw new Error("Expected only one output.");
        }
        return mkOutput(i, dataProcessing, j -> "Data");
    }

    public static DataSourceDescription err_mkOutput(Integer i, DataProcessing dataProcessing) {
        if (i != 0) {
            throw new Error("Expected only one output.");
        }
        return mkOutput(i, dataProcessing, j -> "Error");
    }

    public static DataSourceDescription mkOutput_i(Integer i, DataProcessing dataProcessing) {
        return mkOutput(i, dataProcessing, j -> "Data_" + j);
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
