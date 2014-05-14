package org.pikater.core.dataStructures.options.types;

import java.util.ArrayList;
import java.util.List;

public class OptionList extends AbstractOption {

	private List<Object> list = new ArrayList<Object>();

	public OptionList() {}
	
	public OptionList(List<Object> list) {
		this.list = list;
	}

	@Override
	public Class<? extends Object> getOptionClass() {
		
		if ( list.isEmpty() ) {
			return null;
		}

		Class<? extends Object> objectClass = list.get(0).getClass();
		for (Object object : list) {
			
			if (object.getClass() != objectClass) {
				return null;
			}
		}
		
		return objectClass;
	}

	public List<Object> getList() {
		return list;
	}
	public void setList(List<Object> list) {
		this.list = list;
	}
	public void add(Object object) {
		this.list.add(object);
	}
}
