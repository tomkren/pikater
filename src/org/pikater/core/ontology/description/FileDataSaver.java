package org.pikater.core.ontology.description;

public class FileDataSaver implements IDataSaver {

	private DataSourceDescription dataSource = null;
	private String nameOfFile = "";
	
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

}
