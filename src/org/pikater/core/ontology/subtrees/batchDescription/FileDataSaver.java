package org.pikater.core.ontology.subtrees.batchDescription;

import jade.util.leap.ArrayList;

import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

public class FileDataSaver extends AbstractDataProcessing implements IDataSaver {

	private static final long serialVersionUID = 2763495775498995530L;

	private String nameOfFile;
	private DataSourceDescription dataSource;


	public void setDataSource(DataSourceDescription dataSource) {
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
	UniversalElement exportUniversalElement(
			UniversalComputationDescription uModel) {

		Option nameOfFileOption = new Option();
		nameOfFileOption.setName("nameOfFile");
		nameOfFileOption.setValue(nameOfFile);

	    ArrayList options = new ArrayList();
	    options.add(nameOfFileOption);
	    
	    UniversalConnector universalDataSource =
	    		dataSource.exportUniversalConnector(uModel);
	    universalDataSource.setInputDataType("dataSource");
	    
		UniversalOntology ontologyInfo = new UniversalOntology();
		ontologyInfo.setType(this.getClass());
		ontologyInfo.setOptions(options);
		ontologyInfo.addInputSlot(universalDataSource);

		UniversalElement wrapper = new UniversalElement();
		wrapper.setOntologyInfo(ontologyInfo);
		uModel.addElement(wrapper);
		
		return wrapper;
	}

}
