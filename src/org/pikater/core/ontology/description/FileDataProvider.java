package org.pikater.core.ontology.description;

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

}
