package org.pikater.core.ontology.search.searchItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoolSItem extends SearchItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2149606613979259287L;

	@Override
	public String randomValue(Random rndGen) {
		int rInt2 = rndGen.nextInt(2);
		if (rInt2 == 1) {
			return "True";
		} else {
			return "False";
		}
	}

	@Override
	public List<String> possibleValues() {
		// TODO Auto-generated method stub
		List<String> posVals = new ArrayList<String>();
		posVals.add("True");
		posVals.add("False");
		return posVals;
	}

}
