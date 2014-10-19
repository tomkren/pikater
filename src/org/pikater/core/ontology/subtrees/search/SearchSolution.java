package org.pikater.core.ontology.subtrees.search;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.shared.logging.core.ConsoleLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SearchSolution implements Concept {
	private static final long serialVersionUID = 5183991490097709263L;

	private List<IValueData> values;

	public List<IValueData> getValues() {
		if (values != null) {
			return values;
		} else {
			return new ArrayList<IValueData>();
		}
	}

	public void setValues(List<IValueData> values) {
		this.values = values;
	}

	public void printContent() {
		StringBuilder sb = new StringBuilder();
		boolean start = true;
		for (IValueData valueI : getValues()) {
			if (!start) {
				sb.append(",");
			}
			sb.append(valueI);
			start = false;
		}
		ConsoleLogger.log(Level.INFO, sb.toString());
	}
}