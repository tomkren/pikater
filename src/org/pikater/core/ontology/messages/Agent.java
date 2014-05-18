package org.pikater.core.ontology.messages;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.messages.option.Option;

import jade.content.Concept;


public class Agent implements Concept, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6257129995443147585L;
	private String _name;
	private String _type;
	private List<Option> _options;
	private String _gui_id;
	private byte[] object;

	// Methods required to use this class to represent the OPTIONS role
	public List<Option> getOptions() {
		return _options;
	}
	public void setOptions(List<Option> options) {
		_options = options;
	}

	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}

	public String getType() {
		return _type;
	}
	public void setType(String type) {
		_type = type;
	}

	public String getGui_id() {
		return _gui_id;
	}
	public void setGui_id(String gui_id) {
		_gui_id = gui_id;
	}

	public byte[] getObject() {
		return object;
	}
	public void setObject(byte[] object) {
		this.object = object;
	}

	// -----------------------------

	public List<Option> stringToOptions(String optString) {
		String[] optArray = optString.split("[ ]+");
		List<Option> optList = new ArrayList<Option>();
		for (int i = 0; i < optArray.length; i++) {
			if (optArray[i].startsWith("-")) {
				String name = optArray[i].replaceFirst("-", "");
				// if the next array element is again an option name,
				// (or it is the last element)
				// => it's a boolean parameter

				Option opt = new Option();
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

	public String optionsToString() {

		String str = "";
		if (_options == null) {
			return str;
		}

		for (Option next_opt : _options) {

			if (next_opt.getValue() == null
					|| next_opt.getValue().equals("null")) {
				// don't include this option to the string
			} else {
				if (next_opt.getData_type().equals("BOOLEAN")) {
					if (next_opt.getValue().equals("True")) {
						str += "-" + next_opt.getName() + " ";
					}
				} else {
					str += "-" + next_opt.getName() + " " + next_opt.getValue()
							+ " ";
				}
			}
		}
		return str;
	}

	public Option getOptionByName(String name) {

		for (Option optionI : getOptions()) {
			if (optionI.getName().equals(name)) {
				return optionI;
			}
		}

		return null;
	}

	public String toGuiString() {
		if (_options == null) {
			return "";
		}

		String str = "";
		for (Option optionI : _options) {

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

	public Object clone() {

		Agent agent = new Agent();
		agent.setGui_id(_gui_id);
		agent.setName(_name);
		agent.setObject(object);
		agent.setOptions(_options);
		agent.setType(_type);

		return agent;
	}

}