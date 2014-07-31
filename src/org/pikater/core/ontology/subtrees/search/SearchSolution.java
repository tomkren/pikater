package org.pikater.core.ontology.subtrees.search;

import jade.content.Concept;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

import java.util.ArrayList;
import java.util.List;


public class SearchSolution implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5183991490097709263L;
	private List<IValueData> values;
	public List<IValueData> getValues() {
		if(values!=null)
			return values;
		return new ArrayList<>();
	}

	public void setValues(List<IValueData> values) {
		this.values = values;
	}
	
	public void printContent(){
		
		boolean start = true;
		for (IValueData valueI : getValues() ) {
			if(!start)
				System.out.print(",");
			System.out.print(valueI);
			start = false;
		}
	}
}
