package org.pikater.core.ontology.description;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.bind.annotation.XmlRootElement;

import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.ontology.messages.Option;

import com.thoughtworks.xstream.XStream;

/**
 * Created by Martin Pilat on 28.12.13.
 */
@XmlRootElement
public class ComputationDescription implements Concept {

    IComputationElement rootElement;
    ArrayList<Option> globalOptions;

    public IComputationElement getRootElement() {
        return rootElement;
    }

    public void setRootElement(IComputationElement rootElement) {
        this.rootElement = rootElement;
    }

    public ArrayList<Option> getGlobalOptions() {
        return globalOptions;
    }

    public void setGlobalOptions(ArrayList<Option> globalOptions) {
        this.globalOptions = globalOptions;
    }
    
	public String exportXML(String fileName) throws FileNotFoundException {

		XStream xstream = new XStream();

		Class<ComputationDescription> descriptionOntology =
				org.pikater.core.ontology.description.ComputationDescription.class;
		
		//xstream.aliasPackage("", descriptionOntology.getPackage().getName());
		xstream.aliasAttribute("type", "class");

		String xml = xstream.toXML(this);

		PrintWriter file = new PrintWriter(fileName);
		file.println(xml);
		file.close();

		return xml;
	}

	public static ComputationDescription importXML(String fileName) throws FileNotFoundException {

		XStream xstream = new XStream();

		Class<ComputationDescription> descriptionOntology =
				org.pikater.core.ontology.description.ComputationDescription.class;

		//xstream.aliasPackage("", descriptionOntology.getPackage().getName());
		xstream.aliasAttribute("type", "class");

		Scanner scanner = new Scanner(new File(fileName));
		String xml = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		ComputationDescription computDes =
				(ComputationDescription)xstream.fromXML(xml);
		
		return computDes;
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
