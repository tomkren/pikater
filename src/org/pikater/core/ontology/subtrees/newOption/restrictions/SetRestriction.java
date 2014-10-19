package org.pikater.core.ontology.subtrees.newoption.restrictions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.shared.util.collections.CollectionUtils;

public class SetRestriction implements IRestriction, Iterable<IValueData> {
	private static final long serialVersionUID = 611963641797046162L;

	private boolean nullable;
	private List<IValueData> values;

	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public SetRestriction() {
	}

	@SuppressWarnings("unchecked")
	public SetRestriction(boolean nullable, List<? extends IValueData> values) {
		this.nullable = nullable;
		this.values = (List<IValueData>) values;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public List<IValueData> getValues() {
		return values;
	}

	public void setValues(List<IValueData> values) {
		this.values = values;
	}

	/*
	 * Some convenience interface.
	 */
	public void addValue(IValueData value) {
		this.values.add(value);
	}

	public void addValues(List<IValueData> values) {
		this.values.addAll(values);
	}

	public boolean validatesValue(IValueData otherValue) {
		if (isValid() && fetchSetType().equals(otherValue.getClass())) {
			for (IValueData value : values) {
				if (value.equals(otherValue)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public Class<?> fetchSetType() {
		return values.get(0).getClass();
	}

	public boolean isMasterSetOf(SetRestriction other) {
		if (isValid() && other.isValid()
				&& fetchSetType().equals(other.fetchSetType())) {
			return values.containsAll(other.getValues());
		} else {
			return false;
		}
	}

	/*
	 * ------------------------------------------------------------- CUSTOM
	 * INSTANCE COMPARING - GENERATED WITH ECLIPSE - generate again when you
	 * change local fields or their types - required in {@link
	 * org.pikater.web.vaadin.gui.server.components.forms.OptionValueForm}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (nullable ? 1231 : 1237);
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		SetRestriction other = (SetRestriction) obj;
		if (nullable != other.nullable) {
			return false;
		}
		if (values == null) {
			if (other.values != null) {
				return false;
			}
		} else if (!values.equals(other.values)) {
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	@Override
	public Iterator<IValueData> iterator() {
		return values.iterator();
	}

	@Override
	public SetRestriction clone() {
		SetRestriction result;
		try {
			result = (SetRestriction) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		result.setNullable(nullable);
		result.setValues(CollectionUtils.deepCopy(values));
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isValid() {
		if ((values == null) || values.isEmpty()) {
			return false;
		} else {
			// check if all values are instances of same class
			Set<Class<IValueData>> types = new HashSet<Class<IValueData>>();
			for (IValueData value : values) {
				types.add((Class<IValueData>) value.getClass());
			}
			return types.size() < 2;
		}
	}
}