package org.pikater.core.ontology.description;

import java.util.ArrayList;

/**
 * Created by marti_000 on 27.12.13.
 */
public class DataProcessing implements IDataProvider {

    ArrayList<DataSourceDescription> dataSources;

    public ArrayList<DataSourceDescription> getDataSources() {
        return dataSources;
    }

    public void setDataSources(ArrayList<DataSourceDescription> dataSources) {
        this.dataSources = dataSources;
    }
}
