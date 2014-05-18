package org.pikater.core.ontology.subtrees.description;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

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
	UniversalElement exportUniversalElement(
			UniversalComputationDescription uModel) {

		Option fileURIOption = new Option();
		fileURIOption.setName("fileURI");
		fileURIOption.setValue(fileURI);
		
		List<Option> options = new ArrayList<Option>();
		options.add(fileURIOption);
		
		UniversalOntology element = new UniversalOntology();
		element.setType(this.getClass());
		element.setOptions(options);

		UniversalElement wrapper =
				new UniversalElement(uModel);
		wrapper.setElement(element);
		
		return wrapper;
	}

}
