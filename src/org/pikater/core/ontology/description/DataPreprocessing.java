package org.pikater.core.ontology.description;

import java.util.ArrayList;

/**
 * Created by Stepan on 20.4.14.
 */
public class DataPreprocessing extends AbstractDataProcessing implements IDataProvider {

    ArrayList<DataSourceDescription> dataSources;

    public ArrayList<DataSourceDescription> getDataSources() {
        return dataSources;
    }
    public void setDataSources(ArrayList<DataSourceDescription> dataSources) {
        this.dataSources = dataSources;
    }

}
