package pikater.ontology.description.boxesWrappers;

import pikater.ontology.description.Box;
import pikater.ontology.description.BoxWraper;
import pikater.ontology.description.boxes.providers.IDataProvider;
import jade.content.Concept;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class DataSourceDescription extends BoxWraper {

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
