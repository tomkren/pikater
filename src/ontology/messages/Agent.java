package ontology.messages;

import jade.content.Concept;
import jade.util.leap.*;

public class Agent implements Concept{
	private String _name;
	private List _options;
	

	// Methods required to use this class to represent the OPTIONS role
	public void setOptions(List options) {
		_options=options;
	}
	public List getOptions() {
		return _options;
	}	
	public void setName(String name) {
		_name=name;
	}
	public String getName() {
		return _name;
	}
    
	// -----------------------------
	
	public List stringToOptions(String optString){
    	String[] optArray = optString.split("[ ]+");
		List optList = new ArrayList();
    	for (int i=0; i < optArray.length; i++){
    		if (optArray[i].startsWith("-")){
    			String name = optArray[i].replaceFirst("-", "");
    			// if the next array element is again an option name, 
    			// (or it is the last element)
    			// => it's a boolean parameter
    			String value;
    			if (i == optArray.length-1){
    				value = "True";
    			}
    			else {
    				if (optArray[i+1].startsWith("-")){
    					value = "True";
    				}
    				else{
    					value = optArray[i+1];    				
    				}
    			}
    			Option opt = new Option();
    			opt.setName(name);
    			opt.setValue(value);
    			optList.add(opt);	
    		}
    	}
		return optList;
	}
	
	public String optionsToString(){
		String str = "";
		if(_options == null){
			return str;
		}
		Iterator itr = _options.iterator();	 		   		 
	 	while (itr.hasNext()) {
	 		ontology.messages.Option next_opt = (ontology.messages.Option) itr.next();
	 		if (next_opt.getData_type().equals("BOOLEAN")){
	 			if (next_opt.getValue() == "True"){
	 				str += "-"+next_opt.getName()+" ";	
	 			}
	 		}
	 		else{
	 			str += "-"+next_opt.getName()+" "+next_opt.getValue()+" ";
	 		}
	 	}
	 	return str;
	}
	
}