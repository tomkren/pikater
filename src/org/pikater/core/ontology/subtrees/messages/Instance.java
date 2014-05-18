package org.pikater.core.ontology.subtrees.messages;

import jade.content.Concept;
import jade.util.leap.Iterator;
import java.util.List;

public class Instance implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = -767943628175500132L;
	private List<Double> values;// Double[]
	private List<Boolean> missing;// Boolean[]

	/**
	 * @return the values
	 */
	public List<Double> getValues() {
		return values;
	}
	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(List<Double> values) {
		this.values = values;
	}


	public List<Boolean> getMissing() {
		return missing;
	}

	public void setMissing(List<Boolean> missing) {
		this.missing = missing;
	}

	public void setPrediction(double v) {
		values.remove(values.size() - 1);
		values.add(v);
	}

	// ---------------------
	public String toString(DataInstances _insts) {
		if (values == null) {
			return "\n";
		}
		StringBuffer text = new StringBuffer();

		int i = 0;

		for (int indexI = 0; indexI < values.size(); indexI++) {

			boolean missing = getMissing().get(indexI);
			double value = getValues().get(indexI);
			Attribute attr = _insts.getAttributes().get(indexI);
			
			if (i > 0) {
				text.append(',');
			}
			if (missing) {
				text.append('?');
			} else {
				text.append(attr.stringValue(value));
			}
			i++;
		}
		return text.toString();
	}

	/* index-th value of instance as a string */
	public String toString(DataInstances _insts, int index) {
		if (values == null) {
			return "";
		}
		boolean miss = (Boolean) missing.get(index);
		double value = (Double) values.get(index);
		Attribute attr = (Attribute) _insts.getAttributes().get(index);
		if (miss) {
			return "?";
		} else {
			return attr.stringValue(value);
		}
	}
}