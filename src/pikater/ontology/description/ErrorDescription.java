package pikater.ontology.description;

import jade.content.Concept;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ErrorDescription implements Concept {

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
