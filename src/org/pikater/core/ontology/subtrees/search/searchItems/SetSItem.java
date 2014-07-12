package org.pikater.core.ontology.subtrees.search.searchItems;

import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class SetSItem extends SearchItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7123951122403010638L;
	private List<ITypedValue> set; //List of strings - all possible values
	
	
	public List<ITypedValue> getSet() {
		return set;
	}
	public void setSet(List<ITypedValue> set) {
		this.set = set;
	}
	
	@Override
	public  List<ITypedValue> possibleValues() {
		
		if (set.size() > getNumber_of_values_to_try() ){
            List<ITypedValue> posVals =new ArrayList<>();
			for(int i = 0; i < getNumber_of_values_to_try(); i++)
				posVals.add(set.get(i));
			return posVals;
		}else
			return set;
	}
}
