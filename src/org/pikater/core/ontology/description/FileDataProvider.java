package org.pikater.core.ontology.description;

import jade.util.leap.ArrayList;

import org.pikater.core.ontology.messages.option.Option;
import org.pikater.shared.database.experiment.UniversalComputationDescription;
import org.pikater.shared.database.experiment.UniversalOntology;
import org.pikater.shared.database.experiment.UniversalElement;

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
		
		ArrayList options = new ArrayList();
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
