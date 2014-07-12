package org.pikater.core.ontology.subtrees.newOption;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

public class OptionList implements Concept, Iterable<NewOption>
{
	private static final long serialVersionUID = -8578686409784032991L;
	
	private List<NewOption> options;
	
	public OptionList()
	{
		this.options = new ArrayList<NewOption>();
	}
	
	public OptionList(List<NewOption> list)
	{
		set(list);
	}
	
	@Override
	public Iterator<NewOption> iterator()
	{
		return options.iterator();
	}
	
	public List<NewOption> getAll()
	{
		return options;
	}
	public void set(List<NewOption> list)
	{
		this.options = list;
	}

	public boolean containsOptionWithName(String optionName)
	{
        return getOptionByName(optionName) != null;
	}
    public NewOption getOptionByName(String optionName)
    {
        for (NewOption option : options) {
            if (option.getName().equals(optionName))
                return option;
        }
        return null;
    }
    public void addOption(NewOption option)
    {
        this.options.add(option);
    }
    
    public String exportToWeka() {
    	return OptionList.exportToWeka(options);
    }
    
	public static String exportToWeka(List<NewOption> options) {
		
		String wekaString = "";
		
		for (NewOption optionI : options) {
			wekaString += optionI.exportToWeka() + " ";
		}
		
		return wekaString;
	}
	
	public String exportXML() {

		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);
		
		String xml = xstream.toXML(this);

		return xml;
	}
	
	public static OptionList importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);

		OptionList optionsNew = (OptionList) xstream
				.fromXML(xml);

		return optionsNew;
	}
	
}
