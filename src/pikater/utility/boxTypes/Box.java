package pikater.utility.boxTypes;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

import pikater.ontology.description.Parameter;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UniversalParameter;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalSlot;

public class Box {

	private String name;
	private String agentName;
	private String type;
	private Class ontology;
	private String picture;
	private String description;

	private ArrayList<UniversalParameter> parameters = new ArrayList<UniversalParameter>();

	private ArrayList<UniversalSlot> inputSlots = new ArrayList<UniversalSlot>();
	private ArrayList<UniversalSlot> outputSlots = new ArrayList<UniversalSlot>();


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Class<?> getOntology() {
		return ontology;
	}
	public void setOntology(Class<?> ontology) {
		this.ontology = ontology;
	}

	
	public ArrayList<UniversalParameter> getParameters() {
		return parameters;
	}
	public void setParameters(ArrayList<UniversalParameter> parameters) {
		this.parameters = parameters;
	}
	
	public ArrayList<UniversalSlot> getInputSlots() {
		return inputSlots;
	}
	public void setInputSlots(ArrayList<UniversalSlot> inputSlots) {
		this.inputSlots = inputSlots;
	}

	public ArrayList<UniversalSlot> getOutputSlots() {
		return outputSlots;
	}
	public void setOutputSlots(ArrayList<UniversalSlot> outputSlots) {
		this.outputSlots = outputSlots;
	}
	
	
	public void addParameter(UniversalParameter parameter) {
		this.parameters.add(parameter);
	}

	public void addInputSlot(UniversalSlot slot) {
		this.inputSlots.add(slot);
	}

	public void addOutputSlot(UniversalSlot slot) {
		this.outputSlots.add(slot);
	}

	
	protected void exportXML(File file) throws IOException {

		String thisPackage = "pikater.utility.boxTypes";

		String slotdPackage = "pikater.utility.pikaterDatabase.tables.experiments.slots";
        String parametersPackage = "pikater.utility.pikaterDatabase.tables.experiments.parameters";
        String boxTypesPackage = "pikater.utility.boxTypes";

		XStream xstream = new XStream();
//		xstream.aliasPackage("", thisPackage);
//		xstream.aliasPackage("", slotdPackage);
//		xstream.aliasPackage("", parametersPackage);
//		xstream.aliasPackage("", boxTypesPackage);
		
		String xml = xstream.toXML(this);
		
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter streamWriter = new OutputStreamWriter(fileOutputStream);    
        Writer bufferedWriter = new BufferedWriter(streamWriter);

        bufferedWriter.write(xml);

        bufferedWriter.close();
        streamWriter.close();
        fileOutputStream.close();
	}

	public static Box importXML(File file) throws IOException {
		
		String thisPackage = "pikater.utility.boxTypes";
		
		String slotdPackage = "pikater.utility.pikaterDatabase.tables.experiments.slots";
        String parametersPackage = "pikater.utility.pikaterDatabase.tables.experiments.parameters";
        String boxTypesPackage = "pikater.utility.boxTypes";
		
		FileInputStream  fileStream = new FileInputStream(file);
		BufferedInputStream inputStream = new BufferedInputStream(fileStream);
		DataInputStream dataStream = new DataInputStream(inputStream);

		String xml = "";
		while (dataStream.available() != 0) {

			xml = xml + dataStream.readLine();
		}

		XStream xstream = new XStream();
//		xstream.aliasPackage("", thisPackage);
//		xstream.aliasPackage("", slotdPackage);
//		xstream.aliasPackage("", parametersPackage);
//		xstream.aliasPackage("", boxTypesPackage);

		return (Box)xstream.fromXML(xml);
	}

}
