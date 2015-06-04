package cz.tomkren.pikater;

import org.pikater.core.ontology.subtrees.batchdescription.*;

import java.util.List;

/** Created by tom on 1.6.2015.*/

public class BoxInstance {

    private final int id;
    private final BoxPrototype prototype;
    private final int numIn;
    private final int numOut;

    private DataProcessing dataProcessing;
    private List<DataSourceDescription> outputs;

    public BoxInstance(int id, BoxPrototype prototype, int numIn, int numOut) {
        this.id = id;
        this.prototype = prototype;
        this.numIn = numIn;
        this.numOut = numOut;

        dataProcessing = prototype.mkDataProcessing();
        outputs = prototype.mkOutputs(dataProcessing, numOut);
    }

    public DataSourceDescription getOutputSource(int sourcePort) {
        return outputs.get(sourcePort);
    }

    public void setInputSources(List<DataSourceDescription> sources) {
        prototype.setSources(dataProcessing, sources);
    }


    public int getId() {return id;}
    public BoxPrototype getPrototype() {return prototype;}
    public DataProcessing getDataProcessing() {return dataProcessing;}
    public List<DataSourceDescription> getOutputs() {return outputs;}

    public int getNumIn() {return numIn;}
    public int getNumOut() {return numOut;}
}
