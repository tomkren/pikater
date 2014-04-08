package org.pikater.core.ontology.description;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class FileDataProvider implements IDataProvider {

    public String getFileURI() {
        return fileURI;
    }

    public void setFileURI(String fileURI) {
        this.fileURI = fileURI;
    }

    private String fileURI;

}
