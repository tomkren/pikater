package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pikater.core.ontology.subtrees.batchDescription.export.Slot;
import org.pikater.core.ontology.subtrees.option.Option;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class FileDataProvider extends AbstractDataProcessing implements IDataProvider {

	private static final long serialVersionUID = -7222688693820033064L;

	private String fileURI;


    public String getFileURI() {
        return fileURI;
    }
    public void setFileURI(String fileURI) {
        this.fileURI = fileURI;
    }
	
	@Override
	public List<Option> getUniversalOptions() {
		
		Option fileURIOption = new Option();
		fileURIOption.setName("fileURI");
		fileURIOption.setValue(fileURI);
		
		List<Option> options = new ArrayList<Option>();
		options.add(fileURIOption);
		
		return options;
	}
	@Override
	public void setUniversalOptions(List<Option> options) {
		// TODO Auto-generated method stub
		
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

}
