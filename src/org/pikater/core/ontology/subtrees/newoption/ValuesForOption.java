package org.pikater.core.ontology.subtrees.newoption;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pikater.core.ontology.subtrees.newoption.base.IValidated;
import org.pikater.core.ontology.subtrees.newoption.base.IWekaItem;
import org.pikater.core.ontology.subtrees.newoption.base.Value;
import org.pikater.core.ontology.subtrees.newoption.values.NullValue;
import org.pikater.core.ontology.subtrees.newoption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newoption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.shared.util.ICloneable;
import org.pikater.shared.util.collections.CollectionUtils;

public class ValuesForOption implements Concept, IValidated, ICloneable,
		IWekaItem, Iterable<Value> {
	private static final long serialVersionUID = -3600291732186684079L;

	private List<Value> values;

	/**
	 * Should only be used internally and by JADE.
	 */
	@Deprecated
	public ValuesForOption() {
		this.values = new ArrayList<Value>();
	}

	public ValuesForOption(Value value) {
		this();
		this.values.add(value);
	}

	public ValuesForOption(List<Value> values) {
		this();
		this.values.addAll(values);
	}

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

	/*
	 * Some convenience interface.
	 */
	public void addValue(Value value) {
		this.values.add(value);
	}

	public Value returnByIndex(int index) {
		return values.get(index);
	}

	public int size() {
		return values.size();
	}

	public boolean containsQuestionMark() {
		for (Value valueI : values) {
			IValueData ivalueI = valueI.getCurrentValue();
			if (ivalueI instanceof QuestionMarkRange
					|| ivalueI instanceof QuestionMarkSet) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Inherited interface.
	 */
	@Override
	public ValuesForOption clone() {
		ValuesForOption result;
		try {
			result = (ValuesForOption) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		result.setValues(CollectionUtils.deepCopy(values));
		return result;
	}

	@Override
	public boolean isValid() {
		for (Value value : values) {
			if (!value.isValid()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String exportToWeka() {
		String result = "";
		for (Value valueI : values) {
			if (!(valueI.getCurrentValue() instanceof NullValue)) {
				result += " " + valueI.getCurrentValue().exportToWeka();
			}
		}
		return result;
	}

	@Override
	public Iterator<Value> iterator() {
		return values.iterator();
	}
}