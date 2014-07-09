package org.pikater.core.ontology.subtrees.search;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;
import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;


public class SearchSolution implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5183991490097709263L;
	private List<ITypedValue> values;//list of string-values

	public List<ITypedValue> getValues() {
		if(values!=null)
			return values;
		return new ArrayList<ITypedValue>();
	}

	public void setValues(List<ITypedValue> values) {
		this.values = values;
	}
	
	public void printContent(){
		
		boolean start = true;
		for (ITypedValue valueI : getValues() ) {
			if(!start)
				System.out.print(",");
			System.out.print(valueI);
			start = false;
		}
	}
}
