package org.pikater.core.ontology.subtrees.batchdescription;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

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
		
	@Override
	public List<NewOption> exportAllOptions() {
	    return new ArrayList<NewOption>();
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
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
			throw new IllegalArgumentException("Argument dataSourceDescriptions can be only null");
		} else {
			this.dataSource = dataSourceDescriptions.get(0);
		}
	}
	
	@Override
	public List<ErrorSourceDescription> exportAllErrors() {
		return new ArrayList<ErrorSourceDescription>();
	}
	@Override
	public void importAllErrors(List<ErrorSourceDescription> errors) {

		if (errors != null && !errors.isEmpty()) {
			throw new IllegalArgumentException("Argument errors can be only null");
		}
	}
	
	@Override
	public FileDataSaver clone()
	{
		FileDataSaver fileSaver = (FileDataSaver) super.clone();
		fileSaver.setId(this.getId());
		fileSaver.setNameOfFile(nameOfFile);
		fileSaver.setDataSource(dataSource.clone());
		
		return fileSaver;
	}
	
}
