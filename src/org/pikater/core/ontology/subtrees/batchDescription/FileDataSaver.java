package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.value.StringValue;
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

		NewOption optionNameOfFile = uOntology.getOptionByName("nameOfFile");
		StringValue value = (StringValue) optionNameOfFile.convertToSingleValue().getValue();
		fileDataSaver.setNameOfFile(value.getValue());
		
		
		return fileDataSaver;
	}
	
	@Override
	public List<NewOption> exportAllOptions() {
		
		NewOption nameOfFileOption = new NewOption(
				new StringValue(getNameOfFile()), "nameOfFile");

	    List<NewOption> options = new ArrayList<NewOption>();
	    options.add(nameOfFileOption);

	    return options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		
		for (NewOption optionI : options) {
			if (optionI.getName().equals("nameOfFile")) {
				StringValue value = (StringValue)
						optionI.convertToSingleValue().getValue();
				this.nameOfFile = value.getValue();
			}
		}
		
	}
	
	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {
		
		List<DataSourceDescription> ds = new ArrayList<DataSourceDescription>();
		ds.add(this.dataSource);
		return ds;
	}
	@Override
	public void importAllDataSourceDescriptions(List<DataSourceDescription> dataSourceDescriptions) {
		
		if (dataSourceDescriptions.size() != 1) {
			new IllegalArgumentException("Argument dataSourceDescriptions can be only null");
		} else {
			this.dataSource = dataSourceDescriptions.get(0);
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
