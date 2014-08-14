package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreConstants;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class FileDataProvider extends DataProcessing implements IDataProvider {

	private static final long serialVersionUID = -7222688693820033064L;

	private String fileURI;


    public String getFileURI() {
        return fileURI;
    }
    public void setFileURI(String fileURI) {
        this.fileURI = fileURI;
    }
	
	@Override
	public List<NewOption> exportAllOptions() {
		
		NewOption fileURIOption = new NewOption(CoreConstants.FILEURI, fileURI);
		
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(fileURIOption);
		
		return options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		
		for (NewOption optionI : options) {
			if (optionI.getName().equals(CoreConstants.FILEURI)) {
				StringValue value = (StringValue) optionI.toSingleValue().getCurrentValue();
				this.fileURI = value.getValue();
			}
		}
		
	}
	
	@Override
	public List<ErrorSourceDescription> exportAllErrors() {
		return new ArrayList<ErrorSourceDescription>();
	}
	@Override
	public void importAllErrors(List<ErrorSourceDescription> errors) {
		
		if (errors != null && !errors.isEmpty()) {
			new IllegalArgumentException("Argument errors can be only null");
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

	public FileDataProvider clone() {
		
		FileDataProvider fileData = new FileDataProvider();
		fileData.setId(this.getId());
		fileData.setFileURI(fileURI);
		
		return fileData;
	}

}
