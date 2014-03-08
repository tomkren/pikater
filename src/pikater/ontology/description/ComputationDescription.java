package pikater.ontology.description;

import jade.content.Concept;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.XStream;

/**
 * Created by Martin Pilat on 28.12.13.
 */
@XmlRootElement
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
    
	public String exportXML() {

		XStream xstream = new XStream();		
//		xstream.alias("ComputationDescription",
//				pikater.ontology.description.ComputationDescription.class);
		xstream.aliasPackage("", "pikater.ontology.description");
		
		String xml = xstream.toXML(this);
		xml = xml.replace("class=\"", "type=\"");

		return xml;
	}

	public void importXML(String xml) {

		xml = xml.replace("type=\"", "class=\"");
		
		XStream xstream = new XStream();
		xstream.aliasPackage("", "pikater.ontology.description");
		
		ComputationDescription computDes =
				(ComputationDescription)xstream.fromXML(xml);
		
		setRootElement(computDes.getRootElement());
		setGlobalParameters(computDes.getGlobalParameters());
	}

}
