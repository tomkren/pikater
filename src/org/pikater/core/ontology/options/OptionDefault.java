package org.pikater.core.ontology.options;

import org.pikater.core.ontology.options.types.OptionInterval;
import org.pikater.core.ontology.options.types.OptionList;
import org.pikater.core.ontology.options.types.OptionValue;

public class OptionDefault {

	private String name;
	private String description;

	private OptionValue value = null;
	private OptionInterval interval = null;
	private OptionList list = null;
	
	public OptionDefault() {}

	public OptionDefault(OptionValue value,
			OptionInterval interval, OptionList list) {
		
		this.value = value;
		this.interval = interval;
		this.list = list;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

	public OptionValue getValue() {
		return value;
	}
	public void setValue(OptionValue value) {
		this.value = value;
	}

	public OptionInterval getInterval() {
		return interval;
	}
	public void setInterval(OptionInterval interval) {
		this.interval = interval;
	}

	public OptionList getList() {
		return list;
	}
	public void setList(OptionList list) {
		this.list = list;
	}

}
