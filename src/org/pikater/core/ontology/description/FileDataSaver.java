package org.pikater.core.ontology.description;

public class FileDataSaver implements IDataSaver {

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

}
