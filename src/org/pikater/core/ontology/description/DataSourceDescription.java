package org.pikater.core.ontology.description;

import jade.content.Concept;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class DataSourceDescription implements Concept {

    private IDataProvider dataProvider;
    private String dataType;

    public IDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
