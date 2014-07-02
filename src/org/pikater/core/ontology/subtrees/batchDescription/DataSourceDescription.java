package org.pikater.core.ontology.subtrees.batchDescription;

import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

import jade.content.Concept;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class DataSourceDescription implements Concept {

	private static final long serialVersionUID = 2090764353306584887L;

    private IDataProvider dataProvider;
	private String dataOutputType;
	private String dataInputType;


    public DataSourceDescription() {
    }

    public DataSourceDescription(String fileName) {
    	
        FileDataProvider fileDataProvider = new FileDataProvider();
        fileDataProvider.setFileURI(fileName);

        this.setDataProvider(fileDataProvider);
    }

    public IDataProvider getDataProvider() {
        return dataProvider;
    }
    public void setDataProvider(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public String getDataOutputType() {
        return dataOutputType;
    }
    public void setDataOutputType(String dataType) {
        this.dataOutputType = dataType;
    }
    
    public String getDataInputType() {
		return dataInputType;
	}

	public void setDataInputType(String dataInputType) {
		this.dataInputType = dataInputType;
	}

	UniversalConnector exportUniversalConnector(
    		UniversalComputationDescription uModel) {
    	    	
    	DataProcessing dataProcessing =
    			(DataProcessing) dataProvider;
    	
    	UniversalOntology uOntology =
    			dataProcessing.exportUniversalOntology();
    	UniversalElement universalDataProvider =
    			new UniversalElement();
    	universalDataProvider.setOntologyInfo(uOntology);
    	
    	
    	UniversalConnector connector =
    			new UniversalConnector();
    	connector.setOutputDataType(dataOutputType);
    	connector.setFromElement(universalDataProvider);
    	
    	return connector;
    }

}
