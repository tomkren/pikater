package cz.tomkren.pikater;

import org.pikater.core.CoreConstant;
import org.pikater.core.agents.experiment.computing.Agent_WekaMultilayerPerceptronCA;
import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_KMeans;
import org.pikater.core.agents.experiment.dataprocessing.Agent_PCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_RomanovoU;
import org.pikater.core.agents.experiment.dataprocessing.errorcomputing.Agent_Accuracy;
import org.pikater.core.ontology.subtrees.batchdescription.*;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.Standard;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;

import java.util.List;
import java.util.function.Function;

/** Created by tom on 5. 6. 2015. */

public class BoxUtils {

    private final Converter converter;

    public BoxUtils(String filename) {

        // Jednotlivý krabièky
        BoxPrototype input = new InputPrototype(filename);

        BoxPrototype pca    = new BoxPrototype("PCA", Agent_PCA.class, BoxUtils::pca_mkDataProcessing,BoxUtils::pca_setSources, BoxUtils::pca_mkOutput);
        BoxPrototype kmeans = new BoxPrototype("k-means", Agent_KMeans.class, BoxUtils::mkOutput_i);
        BoxPrototype rbf    = new BoxPrototype("RBF", Agent_WekaRBFNetworkCA.class, BoxUtils::ca_MkDataProcessing, BoxUtils::ca_setSources, BoxUtils::ca_mkOutput);
        BoxPrototype mlp    = new BoxPrototype("MLP", Agent_WekaMultilayerPerceptronCA.class, BoxUtils::ca_MkDataProcessing, BoxUtils::ca_setSources, BoxUtils::ca_mkOutput);
        BoxPrototype u      = new BoxPrototype("U",   Agent_RomanovoU.class, BoxPrototype::mkDataProcessing_default, BoxUtils::setSources_binar, BoxUtils::mkOutput0);

        BoxPrototype err = new BoxPrototype("err", Agent_Accuracy.class, BoxPrototype::mkDataProcessing_default, BoxUtils::err_setSources, BoxUtils::err_mkOutput);
        BoxPrototype output = new BoxPrototype("output", null, BoxUtils::saver_mkDataProcessing, (dp,sources)-> ((FileDataSaver)dp).setDataSource(sources.get(0)), (i,dp)-> null);

        // Vytvoøíme konvertor nadiktováním knihovny krabièek
        converter = new Converter(input, pca, kmeans, rbf, mlp, u, err, output);
    }

    public Converter getConverter() {
        return converter;
    }

    public static ComputationDescription mkComputationDescription(String filename, String... graphLines) {
        BoxUtils bu = new BoxUtils(filename);
        return bu.mkComputationDescription_internal(graphLines);
    }


    private ComputationDescription mkComputationDescription_internal(String... graphLines) {
        try {
            return getConverter().convert(graphLines);
        } catch (Converter.ConverterError converterError) {
            converterError.printStackTrace();
            return null;
        }
    }


    public static DataProcessing pca_mkDataProcessing(String agentType, int id) {
        DataProcessing pca = new DataProcessing();
        pca.setId(id);
        pca.setAgentType(agentType);
        pca.addOption(new NewOption("M",3));  // TODO pak udelat lip aby se proste mohl pøidat i seznam optionù k pøidání
        return pca;
    }

    public static void pca_setSources(DataProcessing pca, List<DataSourceDescription> sources) {
        if (sources.size() != 4) {throw new Error("pca must have 4 sources!");}

        pca.addDataSources(sources.get(0));
        pca.addDataSources(sources.get(1));
        pca.addDataSources(sources.get(2));
        pca.addDataSources(sources.get(3));
    }


    public static String slotIndexToDataType(int i) {
        switch (i) {
            case 0 : return CoreConstant.SlotContent.TRAINING_DATA.getSlotName();
            case 1 : return CoreConstant.SlotContent.TESTING_DATA.getSlotName();
            case 2 : return CoreConstant.SlotContent.VALIDATION_DATA.getSlotName();
            case 3 : return CoreConstant.SlotContent.DATA_TO_LABEL.getSlotName();
            default: throw new Error("i must be \\in {0,...,3}!");
        }
    }

    public static DataSourceDescription pca_mkOutput(Integer i, DataProcessing pca) {
        DataSourceDescription dataSourcePCA = new DataSourceDescription();
        String dataType = slotIndexToDataType(i);
        dataSourcePCA.setOutputType(dataType);
        dataSourcePCA.setInputType(dataType);
        dataSourcePCA.setDataProvider(pca);
        return dataSourcePCA;
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

    public static DataProcessing ca_MkDataProcessing(String agentType, int id) {
        ComputingAgent ca = new ComputingAgent();
        ca.setId(id);
        ca.setAgentType(agentType);
        return ca;
    }

    public static void ca_setSources(DataProcessing dataProcessing, List<DataSourceDescription> sources) {
        //DataSourceDescription source = sources.get(0);
        ComputingAgent ca = (ComputingAgent) dataProcessing;
        ca.setTrainingData  (sources.get(0));
        ca.setTestingData   (sources.get(1));
        ca.setValidationData(sources.get(2));
        ca.setDataToLabel   (sources.get(3));
        ca.setEvaluationMethod(new EvaluationMethod(Standard.class.getName()));
    }

    public static DataSourceDescription ca_mkOutput(Integer i, DataProcessing ca) {

        DataSourceDescription labeledDataSource = new DataSourceDescription();
        labeledDataSource.setInputType(CoreConstant.SlotContent.COMPUTED_DATA.getSlotName());
        labeledDataSource.setOutputType(CoreConstant.SlotContent.COMPUTED_DATA.getSlotName());
        labeledDataSource.setDataProvider(ca);

        return labeledDataSource;
    }


    public static void err_setSources(DataProcessing err, List<DataSourceDescription> sources) {
        DataSourceDescription labeledDataSource = sources.get(0);
        DataSourceDescription fileDataSource = sources.get(1);

        err.addDataSources(labeledDataSource);
        err.addDataSources(fileDataSource);
    }

    public static DataProcessing saver_mkDataProcessing(String agentType, int id) {
        DataProcessing dp = new FileDataSaver();
        dp.setId(id);
        return dp;
    }


    public static void setSources_binar(DataProcessing dataProcessing, List<DataSourceDescription> sources) {
        dataProcessing.addDataSources(sources.get(0));
        dataProcessing.addDataSources(sources.get(1));
    }


}
