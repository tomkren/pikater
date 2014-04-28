package org.pikater.core.ontology.description;

import jade.util.leap.ArrayList;

import org.pikater.core.ontology.messages.Option;
import org.pikater.shared.database.experiment.UniversalElement;
import org.pikater.shared.database.experiment.UniversalElementWrapper;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class FileDataProvider extends AbstractDataProcessing implements IDataProvider {

    private String fileURI;


    public String getFileURI() {
        return fileURI;
    }
    public void setFileURI(String fileURI) {
        this.fileURI = fileURI;
    }

	@Override
	UniversalElementWrapper exportUniversalElement() {

		Option fileURIOption = new Option();
		fileURIOption.setName("fileURI");
		fileURIOption.setValue(fileURI);
		
		ArrayList options = new ArrayList();
		options.add(fileURIOption);
		
		UniversalElement element = new UniversalElement();
		element.setType(this.getClass());
		element.setOptions(options);

		UniversalElementWrapper wrapper =
				new UniversalElementWrapper();
		wrapper.setElement(element);
		
		return wrapper;
	}

}
