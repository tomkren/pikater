package org.pikater.core.ontology.subtrees.newOption;

import java.util.List;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

public class Options implements Concept{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8578686409784032991L;
	private List<NewOption> options;
	
	public void setList(List<NewOption> list) {
		this.options = list;
	}
	public List<NewOption> getList() {
		return options;
	}
	
	public Options(List<NewOption> list){
		setList(list);
	}
	public Options(){
	}

    public NewOption getOption(String optionName)
    {
        for (NewOption option : options) {
            if (option.getName().equals(optionName))
                return option;
        }
        return null;
    }
    
    public String exportToWeka() {
    	return Options.exportToWeka(options);
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
	
	public static Options importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);

		Options optionsNew = (Options) xstream
				.fromXML(xml);

		return optionsNew;
	}
	
}
