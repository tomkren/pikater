package org.pikater.core.ontology.description;

import jade.util.leap.ArrayList;

import org.pikater.core.ontology.messages.Option;
import org.pikater.shared.database.experiment.UniversalConnector;
import org.pikater.shared.database.experiment.UniversalElement;
import org.pikater.shared.database.experiment.UniversalElementWrapper;

public class FileDataSaver extends AbstractDataProcessing implements IDataSaver {

	private String nameOfFile;
	private DataSourceDescription dataSource;


	public void  setDataSource(DataSourceDescription dataSource) {
		this.dataSource = dataSource;
	}
	public DataSourceDescription getDataSource() {
		return dataSource;
	}

	public String getNameOfFile() {
		return nameOfFile;
	}
	public void setNameOfFile(String nameOfFile) {
		this.nameOfFile = nameOfFile;
	}

	@Override
	UniversalElementWrapper exportUniversalElement() {

		Option nameOfFileOption = new Option();
		nameOfFileOption.setName("nameOfFile");
		nameOfFileOption.setValue(nameOfFile);

	    ArrayList options = new ArrayList();
	    options.add(nameOfFileOption);
	    
	    UniversalConnector universalDataSource =
	    		dataSource.exportUniversalConnector();
	    universalDataSource.setInputDataType("dataSource");
	    
		UniversalElement element = new UniversalElement();
		element.setType(this.getClass());
		element.setOptions(options);
		element.addInputSlot(universalDataSource);

		UniversalElementWrapper wrapper = new UniversalElementWrapper();
		wrapper.setElement(element);
		
		return wrapper;
	}

}
