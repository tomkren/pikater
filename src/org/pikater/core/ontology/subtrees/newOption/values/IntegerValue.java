package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;

public class IntegerValue implements IComparableValueData {
	private static final long serialVersionUID = -2925380308174903951L;

	private int value;

	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public IntegerValue() {
	}

	public IntegerValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/*
	 * -------------------------------------------------------------
	 * CUSTOM INSTANCE COMPARING - GENERATED WITH ECLIPSE
	 * - generate again when you change local fields or their types
	 * - required in {@link org.pikater.web.vaadin.gui.server.components.forms.OptionValueForm}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		IntegerValue other = (IntegerValue) obj;
		if (value != other.value) {
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	@Override
	public Integer hackValue() {
		return value;
	}

	@Override
	public IntegerValue clone() {
		IntegerValue result;
		try {
			result = (IntegerValue) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		result.setValue(value);
		return result;
	}

	@Override
	public String exportToWeka() {

		return String.valueOf(value);
	}

	@Override
	public String toDisplayName() {
		return "Integer";
	}

	@Override
	public int compareTo(IComparableValueData o) {
		return hackValue().compareTo((Integer) o.hackValue());
	}
}
