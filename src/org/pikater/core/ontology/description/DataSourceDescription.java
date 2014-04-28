package org.pikater.core.ontology.description;

import org.pikater.shared.database.experiment.UniversalConnector;
import org.pikater.shared.database.experiment.UniversalElement;
import org.pikater.shared.database.experiment.UniversalElementWrapper;

import jade.content.Concept;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class DataSourceDescription implements Concept {

    private String dataType;
    private IDataProvider dataProvider;

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
    
    UniversalConnector exportUniversalConnector() {
    	    	
    	AbstractDataProcessing dataProcessing =
    			(AbstractDataProcessing) dataProvider;
    	
    	UniversalElementWrapper universalDataProvider =
    			dataProcessing.exportUniversalElement();
    	
    	UniversalConnector connector =
    			new UniversalConnector();
    	connector.setOutputDataType(dataType);
    	connector.setUniversalDataProvider(universalDataProvider);
    	
    	return connector;
    }

}
