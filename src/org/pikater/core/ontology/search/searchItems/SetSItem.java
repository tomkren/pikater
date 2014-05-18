package org.pikater.core.ontology.search.searchItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class SetSItem extends SearchItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7123951122403010638L;
	private List<String> set; //List of strings - all possible values
	
	
	public List<String> getSet() {
		return set;
	}
	public void setSet(List<String> set) {
		this.set = set;
	}

	@Override
	public String randomValue(Random rnd_gen) {
		// TODO Auto-generated method stub
		int index = rnd_gen.nextInt(set.size());
		return set.get(index).toString();//?toString?
	}
	
	@Override
	public  List<String> possibleValues() {
		
		if (set.size() > getNumber_of_values_to_try() ){
			List<String> posVals = new ArrayList<String>();
			for(int i = 0; i < getNumber_of_values_to_try(); i++)
				posVals.add(set.get(i));
			return posVals;
		}else
			return set;		
	}
}
