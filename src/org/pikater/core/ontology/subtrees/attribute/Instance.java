package org.pikater.core.ontology.subtrees.attribute;

import jade.content.Concept;

import java.util.List;

import org.pikater.core.ontology.subtrees.datainstance.DataInstances;

public class Instance implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = -767943628175500132L;
	private List<Double> values;
	private List<Boolean> missing;

    private List<Double> privateValues;

    private int Id;

    private int IDIdx;

    public int getId() {
        return Id;
    }

    public void setId(int ID) {
        this.Id = ID;
        if (this.privateValues != null)                //should be null only while reconstructing from ontology
            this.privateValues.set(IDIdx, 1.0 * ID);
    }

    public int getIDIdx() {
        return IDIdx;
    }

    public void setIDIdx(int IDIdx) {
        this.IDIdx = IDIdx;
    }

    /**
	 * @return the values
	 */
	public List<Double> getValues() {
		return values;
	}

	public Double getValue(int i) {
		return values.get(i);
	}

    public List<Double> getPrivateValues() {
        return privateValues;
    }

    public Double getPrivateValue(int i) {
        return privateValues.get(i);
    }

	/**
	 * the values to set
	 */
	public void setValues(List<Double> values) {
		this.values = values;
	}

    public void setPrivateValues(List<Double> values) {
        this.privateValues = values;
    }

	public List<Boolean> getMissing() {
		return missing;
	}

	public void setMissing(List<Boolean> missing) {
		this.missing = missing;
	}

	//TODO: repair in case we use different class index
	public void setPrediction(double v) {
		values.remove(values.size() - 1);
		values.add(v);
	}

	private boolean isValueMissing(int index) {
		return getMissing().get(index);
	}

	// ---------------------
	public String toString(DataInstances insts) {
		if (values == null) {
			return "\n";
		}
		StringBuilder text = new StringBuilder();

		int i = 0;

		for (int indexI = 0; indexI < values.size(); indexI++) {

			double value = getValues().get(indexI);
			Attribute attr = insts.getAttributes().get(indexI);

			if (i > 0) {
				text.append(',');
			}
			if (isValueMissing(indexI)) {
				text.append('?');
			} else {
				text.append(attr.stringValue(value));
			}
			i++;
		}
		return text.toString();
	}

	/**
	 * index-th value of instance as a string
	 */
	public String toString(DataInstances insts, int index) {
		if (values == null) {
			return "";
		}
		boolean miss = (Boolean) missing.get(index);
		double value = (Double) values.get(index);
		Attribute attr = (Attribute) insts.getAttributes().get(index);
		if (miss) {
			return "?";
		} else {
			return attr.stringValue(value);
		}
	}
}
