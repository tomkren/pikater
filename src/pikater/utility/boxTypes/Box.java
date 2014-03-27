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

import pikater.utility.pikaterDatabase.tables.experiments.parameters.UniversalParameter;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalSlot;

public class Box
{
	// TODO: name and type can be merged into a single field using the BoxesModel.BoxCategory enum
	
	private final String name;
	private final String agentName;
	private final String type;
	private final Class ontology;
	private final String picture;
	private final String description;

	private final ArrayList<UniversalParameter> parameters;
	private final ArrayList<UniversalSlot> inputSlots;
	private final ArrayList<UniversalSlot> outputSlots;
	
	public Box(String name, String agentName, String type, Class ontology, String picture, String description)
	{
		super();
		
		this.name = name;
		this.agentName = agentName;
		this.type = type;
		this.ontology = ontology;
		this.picture = picture;
		this.description = description;
		
		this.parameters = new ArrayList<UniversalParameter>();
		this.inputSlots = new ArrayList<UniversalSlot>();
		this.outputSlots = new ArrayList<UniversalSlot>();
	}

	// ---------------------------------------------------------------------------
	// PUBLIC GETTERS

	public String getName()
	{
		return name;
	}

	public String getAgentName()
	{
		return agentName;
	}

	public String getType()
	{
		return type;
	}

	public Class getOntology()
	{
		return ontology;
	}

	public String getPicture()
	{
		return picture;
	}

	public String getDescription()
	{
		return description;
	}
	
	public ArrayList<UniversalParameter> getParameters()
	{
		return parameters;
	}

	public ArrayList<UniversalSlot> getInputSlots()
	{
		return inputSlots;
	}

	public ArrayList<UniversalSlot> getOutputSlots()
	{
		return outputSlots;
	}

	// ---------------------------------------------------------------------------
	// OTHER PUBLIC METHODS

	public void addParameter(UniversalParameter parameter)
	{
		this.parameters.add(parameter);
	}

	public void addInputSlot(UniversalSlot slot)
	{
		this.inputSlots.add(slot);
	}

	public void addOutputSlot(UniversalSlot slot)
	{
		this.outputSlots.add(slot);
	}
	
	// ---------------------------------------------------------------------------
	// IMPORT/EXPORT

	protected void exportXML(File file) throws IOException
	{
		/*
		String thisPackage = "pikater.utility.boxTypes";
		String slotdPackage = "pikater.utility.pikaterDatabase.tables.experiments.slots";
		String parametersPackage = "pikater.utility.pikaterDatabase.tables.experiments.parameters";
		String boxTypesPackage = "pikater.utility.boxTypes";
		*/

		XStream xstream = new XStream();
		// xstream.aliasPackage("", thisPackage);
		// xstream.aliasPackage("", slotdPackage);
		// xstream.aliasPackage("", parametersPackage);
		// xstream.aliasPackage("", boxTypesPackage);

		String xml = xstream.toXML(this);

		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter streamWriter = new OutputStreamWriter(fileOutputStream);
		Writer bufferedWriter = new BufferedWriter(streamWriter);

		bufferedWriter.write(xml);

		bufferedWriter.close();
		streamWriter.close();
		fileOutputStream.close();
	}

	public static Box importXML(File file) throws IOException
	{
		/*
		String thisPackage = "pikater.utility.boxTypes";
		String slotdPackage = "pikater.utility.pikaterDatabase.tables.experiments.slots";
		String parametersPackage = "pikater.utility.pikaterDatabase.tables.experiments.parameters";
		String boxTypesPackage = "pikater.utility.boxTypes";
		*/

		DataInputStream dataStream = null;
		try
		{
			dataStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			
			String xml = "";
			while (dataStream.available() != 0)
			{

				xml = xml + dataStream.readLine();
			}

			XStream xstream = new XStream();
			// xstream.aliasPackage("", thisPackage);
			// xstream.aliasPackage("", slotdPackage);
			// xstream.aliasPackage("", parametersPackage);
			// xstream.aliasPackage("", boxTypesPackage);

			return (Box) xstream.fromXML(xml);

		}
		finally
		{
			if(dataStream != null)
			{
				dataStream.close();
			}
		}
	}
}
