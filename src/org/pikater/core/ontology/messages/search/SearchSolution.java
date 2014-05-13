package org.pikater.core.ontology.messages.search;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;


public class SearchSolution implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5183991490097709263L;
	private List<String> values;//list of string-values

	public List<String> getValues() {
		if(values!=null)
			return values;
		return new ArrayList<String>();
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public void printContent(){
		
		boolean start = true;
		for (String valueI : getValues() ) {
			if(!start)
				System.out.print(",");
			System.out.print(valueI);
			start = false;
		}
	}
}
