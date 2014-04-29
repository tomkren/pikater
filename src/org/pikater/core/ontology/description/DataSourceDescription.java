package org.pikater.core.ontology.description;

import org.pikater.shared.database.experiment.UniversalComputationDescription;
import org.pikater.shared.database.experiment.UniversalConnector;
import org.pikater.shared.database.experiment.UniversalElement;

import jade.content.Concept;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class DataSourceDescription implements Concept {

	private static final long serialVersionUID = 2090764353306584887L;

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
    
    UniversalConnector exportUniversalConnector(
    		UniversalComputationDescription uModel) {
    	    	
    	AbstractDataProcessing dataProcessing =
    			(AbstractDataProcessing) dataProvider;
    	
    	UniversalElement universalDataProvider =
    			dataProcessing.exportUniversalElement(uModel);
    	
    	UniversalConnector connector =
    			new UniversalConnector();
    	connector.setOutputDataType(dataType);
    	connector.setUniversalDataProvider(universalDataProvider);
    	
    	return connector;
    }

}
