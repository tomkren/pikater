package org.pikater.core.ontology.subtrees.management;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOption;

import jade.content.Concept;


public class Agent implements Concept, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6257129995443147585L;
	private String name;
	private String type;
	private List<NewOption> options;
	private byte[] object;

	// Methods required to use this class to represent the OPTIONS role
	public List<NewOption> getOptions() {
		return options;
	}
	public void setOptions(List<NewOption> options) {
		this.options = options;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public byte[] getObject() {
		return object;
	}
	public void setObject(byte[] object) {
		this.object = object;
	}

	// -----------------------------
/*
	public List<NewOption> stringToOptions(String optString) {
		String[] optArray = optString.split("[ ]+");
		List<NewOption> optList = new ArrayList<NewOption>();
		for (int i = 0; i < optArray.length; i++) {
			if (optArray[i].startsWith("-")) {
				String name = optArray[i].replaceFirst("-", "");
				// if the next array element is again an option name,
				// (or it is the last element)
				// => it's a boolean parameter

				NewOption opt = new NewOption();
				String value;
				if (i == optArray.length - 1) {
					value = "True";
					opt.setData_type("BOOLEAN");
				} else {
					// if (optArray[i+1].startsWith("-")){
					if (optArray[i + 1].matches("\\-[A-Z]")) {
						value = "True";
						opt.setData_type("BOOLEAN");
					} else {
						value = optArray[i + 1];
						opt.setData_type("FLOAT");
					}
				}

				opt.setName(name);
				opt.setValue(value);

				if (opt.getUser_value() == null) {
					// first string -> options, parsing the string from user
					if (value.contains("?")) {
						opt.setMutable(true);
					}
					opt.setUser_value(value);

				}
				optList.add(opt);
			}
		}
		return optList;
	}
*/
	/*
	public String optionsToString() {

		String wekaString = "";
		if (options == null) {
			return wekaString;
		}

		for (NewOption optionI : options) {
			

			if (optionI.getValue() == null
					|| optionI.getValue().equals("null")) {
				// don't include this option to the string
			} else {
				if (optionI.getData_type().equals("BOOLEAN")) {
					if (optionI.getValue().equals("True")) {
						str += "-" + optionI.getName() + " ";
					}
				} else {
					str += "-" + optionI.getName() + " " + optionI.getValue()
							+ " ";
				}
			}

		}
		return wekaString;
	}
*/
	
	public NewOption getOptionByName(String name) {

		for (NewOption optionI : getOptions()) {
			if (optionI.getName().equals(name)) {
				return optionI;
			}
		}

		return null;
	}
/*
	public String toGuiString() {
		if (options == null) {
			return "";
		}

		String str = "";
		for (NewOption optionI : options) {

			if (optionI.getData_type().equals("BOOLEAN")) {
				if (optionI.getValue().equals("True")) {
					str += "-" + optionI.getName() + " ";
				}
				if (optionI.getMutable()) {
					str += "-" + optionI.getName() + " ? ";
				}
			} else

			if (!optionI.getMutable())
				str += "-" + optionI.getName() + " " + optionI.getValue()
						+ " ";
			else {
				str += "-" + optionI.getName() + " " + optionI.getValue()
						+ "/";
				if (!optionI.getIs_a_set())
					str += "<" + optionI.getRange().getMin() + ","
							+ optionI.getRange().getMax() + ">";
				else {
					String set = "";
					for (int i = 0; i < optionI.getSet().size(); i++) {
						set += optionI.getSet().get(i);
						if (i != optionI.getSet().size() - 1) {
							set += " ";
						}
					}
					str += "{" + set + "}";
				}
				str += "/" + optionI.getNumber_of_values_to_try() + " ";
			}
		}
		return str;
	}
*/
	public Object clone() {

		Agent agent = new Agent();
		agent.setName(name);
		agent.setObject(object);
		agent.setOptions(options);
		agent.setType(type);

		return agent;
	}

}