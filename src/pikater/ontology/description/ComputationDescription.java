package pikater.ontology.description;

import jade.content.Concept;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class ComputationDescription implements Concept {

    IComputationElement rootElement;
    ArrayList<Parameter> globalParameters;

    public IComputationElement getRootElement() {
        return rootElement;
    }

    public void setRootElement(IComputationElement rootElement) {
        this.rootElement = rootElement;
    }

    public ArrayList<Parameter> getGlobalParameters() {
        return globalParameters;
    }

    public void setGlobalParameters(ArrayList<Parameter> globalParameters) {
        this.globalParameters = globalParameters;
    }
}
