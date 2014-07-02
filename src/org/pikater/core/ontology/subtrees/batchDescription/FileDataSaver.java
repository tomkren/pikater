package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.batchDescription.export.Slot;
import org.pikater.core.ontology.subtrees.option.Option;
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
	
	static FileDataSaver importUniversalElement(
			UniversalElement uElement) {
		
		UniversalOntology uOntology = uElement.getOntologyInfo();
		
		FileDataSaver fileDataSaver = new FileDataSaver();

		Option optionNameOfFile = uOntology.getOptionByName("nameOfFile");
		fileDataSaver.setNameOfFile(optionNameOfFile.getValue());
		
		
		return fileDataSaver;
	}
	
	@Override
	public List<Option> getUniversalOptions() {
		
		Option nameOfFileOption = new Option();
		nameOfFileOption.setName("nameOfFile");
		nameOfFileOption.setValue(getNameOfFile());

	    List<Option> options = new ArrayList<Option>();
	    options.add(nameOfFileOption);

	    return options;
	}
	@Override
	public void setUniversalOptions(List<Option> options) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Slot> getInputSlots() {
		return new ArrayList<Slot>();
	}
	@Override
	public void setUniversalInputSlots(List<Slot> universalInputSlots) {
		
		if (universalInputSlots != null && !universalInputSlots.isEmpty()) {
			new IllegalArgumentException("Argument universalInputSlots can be only null");
		}
	}
	
	@Override
	public List<ErrorDescription> getUniversalErrors() {
		return new ArrayList<ErrorDescription>();
	}
	@Override
	public void setUniversalErrors(List<ErrorDescription> errors) {

		if (errors != null && !errors.isEmpty()) {
			new IllegalArgumentException("Argument errors can be only null");
		}
	}
	
}
