package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.value.StringValue;

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
		
		NewOption fileURIOption = new NewOption(
				new StringValue(fileURI), "fileURI");
		
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(fileURIOption);
		
		return options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		
		for (NewOption optionI : options) {
			if (optionI.getName().equals("fileURI")) {
				StringValue value = (StringValue) optionI.getValues().get(0).getValue();
				this.fileURI = value.getValue();
			}
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

}
