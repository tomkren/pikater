package pikater.ontology.description.boxesWrappers;

import pikater.ontology.description.BoxWraper;
import pikater.ontology.description.boxes.providers.IErrorProvider;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ErrorDescription extends BoxWraper {

    IErrorProvider provider;
    String type;

    public IErrorProvider getProvider() {
        return provider;
    }

    public void setProvider(IErrorProvider provider) {
        this.provider = provider;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
