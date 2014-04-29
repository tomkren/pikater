package org.pikater.shared.database.experiment;


import java.io.FileNotFoundException;

import jade.util.leap.ArrayList;


import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.examples.SearchOnly;

import com.thoughtworks.xstream.XStream;

public class UniversalComputationDescription {

	private ArrayList globalOptions;

	private java.util.ArrayList<UniversalElement> rootElements;
	private java.util.ArrayList<UniversalElement> elements;


	public ArrayList getGlobalOptions() {
		return globalOptions;
	}
	public void setGlobalOptions(ArrayList globalOptions) {
		this.globalOptions = globalOptions;
	}

    public java.util.ArrayList<UniversalElement> getRootElements() {

        return rootElements;
    }
    void addRootElement(UniversalElement rootElement) {
    	
    	if (rootElements == null) {
    		rootElements = new java.util.ArrayList<UniversalElement>();
    	}
        this.rootElements.add(rootElement);
    }
    void addElement(UniversalElement element) {
    	
    	if (elements == null) {
    		elements = new java.util.ArrayList<UniversalElement>();
    	}
    	
    	elements.add(element);
    }

	
	
	public String exportXML() {

		XStream xstream = new XStream();
		String xml = xstream.toXML(this);

		return xml;
	}
	
	public static UniversalComputationDescription importXML(String xml) throws FileNotFoundException {

		XStream xstream = new XStream();
		
		UniversalComputationDescription uComputDes =
				(UniversalComputationDescription)xstream.fromXML(xml);
		
		return uComputDes;
	}
	
	public static void main(String [ ] args) {
		
		ComputationDescription description =
				SearchOnly.createDescription();
		
        UniversalComputationDescription uDescription =
        		description.ExportUniversalComputationDescription();
        
        String xml = uDescription.exportXML();
        System.out.println(xml);

	}

}
