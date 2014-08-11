package org.pikater.core.ontology.subtrees.newOption;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.ICloneable;
import org.pikater.core.ontology.subtrees.newOption.base.IMergeable;
import org.pikater.core.ontology.subtrees.newOption.base.IWekaItem;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

public class NewOptions implements Concept, ICloneable, IMergeable, IWekaItem, Iterable<NewOption>
{
	private static final long serialVersionUID = -8578686409784032991L;
	
	private List<NewOption> options;
	
	public NewOptions()
	{
		this.options = new ArrayList<NewOption>();
	}
	public NewOptions(List<NewOption> list)
	{
		setOptions(list);
	}
	
	public List<NewOption> getOptions()
	{
		return options;
	}
	public void setOptions(List<NewOption> list)
	{
		this.options = list;
	}

	public boolean containsOptionWithName(String optionName)
	{
        return fetchOptionByName(optionName) != null;
	}
    public NewOption fetchOptionByName(String optionName)
    {
        for (NewOption option : options)
        {
            if (option.getName().equals(optionName))
            {
                return option;
            }
        }
        return null;
    }
    public void addOption(NewOption option)
    {
        this.options.add(option);
    }
    public void addOptions(List<NewOption> options)
    {
        this.options.addAll(options);
    }

    public void removeOptionByName(String optionName)
    {
    	NewOption option = fetchOptionByName(optionName);
    	this.options.remove(option);
    }

    
	public String exportXML() {

		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);
		
		String xml = xstream.toXML(this);

		return xml;
	}
	public static NewOptions importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);

		NewOptions optionsNew = (NewOptions) xstream
				.fromXML(xml);

		return optionsNew;
	}
    
    @Override
    public NewOptions clone()
    {
    	NewOptions result = new NewOptions();
    	for (NewOption optionI : options)
    	{
    		result.addOption(optionI.clone());
    	}
    	return result;
    }
    @Override
	public void mergeWith(IMergeable other)
	{
    	NewOptions otherOptionsWrapper = (NewOptions) other; 
    	for(NewOption optionInOther : otherOptionsWrapper.getOptions())
		{
			NewOption optionLocal = fetchOptionByName(optionInOther.getName());
			if(optionLocal == null)
			{
				throw new IllegalArgumentException("Could not merge option with name: " + optionInOther.getName());
			}
			else
			{
				optionLocal.mergeWith(optionInOther);
			}
		}
	}
	@Override
	public Iterator<NewOption> iterator()
	{
		return options.iterator();
	}
    @Override
    public String exportToWeka()
    {
    	return NewOptions.exportToWeka(options);
    }
	public static String exportToWeka(List<NewOption> options)
	{
		String wekaString = "";
		for (NewOption optionI : options)
		{
			wekaString += optionI.exportToWeka() + " ";
		}
		return wekaString;
	}
}