package pikater.ontology.description.boxes.dataProcessing;

import java.util.ArrayList;

import pikater.ontology.description.Box;
import pikater.ontology.description.boxes.providers.IDataProvider;
import pikater.ontology.description.boxesWrappers.DataSourceDescription;

/**
 * Created by marti_000 on 27.12.13.
 */
public class DataProcessing extends Box implements IDataProvider {

    ArrayList<DataSourceDescription> dataSources;

    public ArrayList<DataSourceDescription> getDataSources() {
        return dataSources;
    }

    public void setDataSources(ArrayList<DataSourceDescription> dataSources) {
        this.dataSources = dataSources;
    }
}
