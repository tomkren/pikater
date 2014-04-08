package org.pikater.core.ontology.description;

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

	public String exportJSON() {
/*
		Reflections reflections = new Reflections("pikater");

		Set<Class<? extends PikaterAgent>> allClassesAgents = 
			     reflections.getSubTypesOf(pikater.agents.PikaterAgent.class);
		
		List<Class<? extends PikaterAgent>> listAgents =
				new ArrayList<Class<? extends PikaterAgent>>();
		listAgents.addAll(allClassesAgents);

		for (int i = 0; i < listAgents.size(); i++) {

			Class<? extends PikaterAgent> classI = listAgents.get(i);
			String classNameI = classI.toString();
			classNameI = classNameI.substring("class ".length());

			Class<?> c = null;
			try {
				c = Class.forName(classNameI);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			Method lMethod = null;
			try {
				lMethod = c.getDeclaredMethod("getAgentType", null);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			
			System.out.println("method = " + lMethod.toString());			
		}
*/
		return "Still not implemented";
	}
	
}
