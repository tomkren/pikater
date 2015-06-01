package cz.tomkren.pikater;

import org.pikater.core.ontology.subtrees.batchdescription.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/** Created by tom on 1.6.2015.*/

public class BoxPrototype {

    private String name;
    private String agentType;

    private final Function<String,DataProcessing> mkDataProcessingFun;
    private final BiConsumer<DataProcessing,List<DataSourceDescription>> setSourcesFun;
    private final BiFunction<Integer,DataProcessing,DataSourceDescription> mkOutputFun;


    public BoxPrototype(String name, Class<?> agentClass,
                        Function<String,DataProcessing> mkDataProcessingFun,
                        BiConsumer<DataProcessing,List<DataSourceDescription>> setSourcesFun,
                        BiFunction<Integer,DataProcessing,DataSourceDescription> mkOutputFun) {
        this.name = name;
        this.agentType = agentClass.getName();
        this.mkDataProcessingFun = mkDataProcessingFun;
        this.setSourcesFun = setSourcesFun;
        this.mkOutputFun = mkOutputFun;
    }

    public BoxPrototype(String name, Class<?> agentClass, BiFunction<Integer,DataProcessing,DataSourceDescription> mkOutputFun) {
        this(name, agentClass, BoxPrototype::mkDataProcessing_default, BoxPrototype::setSources_default, mkOutputFun);
    }

    public String getName() {return name;}


    public DataProcessing mkDataProcessing() {
        return mkDataProcessingFun.apply(agentType);
    }

    public void setSources(DataProcessing dataProcessing, List<DataSourceDescription> sources) {
        setSourcesFun.accept(dataProcessing, sources);
    }


    public List<DataSourceDescription> mkOutputs(DataProcessing dataProcessing, int numOut) {
        List<DataSourceDescription> outputs = new ArrayList<>(numOut);

        for (int i = 0; i < numOut; i++) {

            /*DataSourceDescription output = new DataSourceDescription();
            output.setOutputType("out_"+i);
            output.setDataProvider(dataProcessing);*/

            DataSourceDescription output = mkOutputFun.apply(i,dataProcessing);
            outputs.add(output);
        }

        return outputs;
    }


    public static DataProcessing mkDataProcessing_default(String agentType) {
        DataProcessing dataProcessing = new DataProcessing();
        dataProcessing.setAgentType(agentType);
        return dataProcessing;
    }

    public static void setSources_default(DataProcessing dataProcessing, List<DataSourceDescription> sources) {
        dataProcessing.addDataSources(sources.get(0));
    }


}
