package pikater.ontology.description.boxes.providers;

import pikater.ontology.description.Box;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class FileDataProvider extends Box implements IDataProvider {

    public String getFileURI() {
        return fileURI;
    }

    public void setFileURI(String fileURI) {
        this.fileURI = fileURI;
    }

    private String fileURI;

}
