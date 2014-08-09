package org.pikater.core.ontology.subtrees.search.searchItems;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

import java.util.ArrayList;
import java.util.List;



public class SetSItem extends SearchItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7123951122403010638L;
	private List<IValueData> set; //List of strings - all possible values
	
	
	public List<IValueData> getSet() {
		return set;
	}
	public void setSet(List<IValueData> set) {
		this.set = set;
	}
	
	@Override
	public  List<IValueData> possibleValues() {
		
		if (set.size() > getNumber_of_values_to_try() ){
            List<IValueData> posVals =new ArrayList<IValueData>();
			for(int i = 0; i < getNumber_of_values_to_try(); i++)
				posVals.add(set.get(i));
			return posVals;
		}else
			return set;
	}
}
