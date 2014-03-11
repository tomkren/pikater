package pikater.ontology.description.boxesWrappers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.reflections.Reflections;

import pikater.ontology.description.Box;
import pikater.ontology.description.BoxWraper;
import pikater.ontology.description.IBox;
import pikater.ontology.description.IBoxWraper;
import pikater.ontology.description.IElement;
import pikater.ontology.description.boxes.IComputationElement;
import pikater.ontology.description.elements.Parameter;

import com.thoughtworks.xstream.XStream;

/**
 * Created by Martin Pilat on 28.12.13.
 */
@XmlRootElement
public class ComputationDescription extends BoxWraper {

    public  IComputationElement rootElement;
    public ArrayList<Parameter> globalParameters;

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
