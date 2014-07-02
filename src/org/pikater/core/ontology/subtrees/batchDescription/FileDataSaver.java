package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

public class FileDataSaver extends DataProcessing implements IDataSaver {

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
	
	static FileDataSaver importUniversalElement(
			UniversalElement uElement) {
		
		UniversalOntology uOntology = uElement.getOntologyInfo();
		
		FileDataSaver fileDataSaver = new FileDataSaver();

		Option optionNameOfFile = uOntology.getOptionByName("nameOfFile");
		fileDataSaver.setNameOfFile(optionNameOfFile.getValue());
		
		
		return fileDataSaver;
	}
	
	@Override
	public List<Option> exportAllOptions() {
		
		Option nameOfFileOption = new Option();
		nameOfFileOption.setName("nameOfFile");
		nameOfFileOption.setValue(getNameOfFile());

	    List<Option> options = new ArrayList<Option>();
	    options.add(nameOfFileOption);

	    return options;
	}
	@Override
	public void importAllOptions(List<Option> options) {
		
		for (Option optionI : options) {
			if (optionI.getName().equals("nameOfFile")) {
				this.nameOfFile = optionI.getValue();
			}
		}
		
	}
	
	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {
		return new ArrayList<DataSourceDescription>();
	}
	@Override
	public void importAllDataSourceDescriptions(List<DataSourceDescription> dataSourceDescriptions) {
		
		if (dataSourceDescriptions != null && !dataSourceDescriptions.isEmpty()) {
			new IllegalArgumentException("Argument dataSourceDescriptions can be only null");
		}
	}
	
	@Override
	public List<ErrorDescription> exportAllErrors() {
		return new ArrayList<ErrorDescription>();
	}
	@Override
	public void importAllErrors(List<ErrorDescription> errors) {

		if (errors != null && !errors.isEmpty()) {
			new IllegalArgumentException("Argument errors can be only null");
		}
	}
	
}
