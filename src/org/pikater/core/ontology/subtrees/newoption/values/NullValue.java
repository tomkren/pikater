package org.pikater.core.ontology.subtrees.newoption.values;

import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;

public class NullValue implements IValueData {
	private static final long serialVersionUID = 4240750027791781820L;

	public NullValue() {
	}

	@Override
	public int hashCode() {
		return 0;
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
		// needed in {@link NewOption#mergeWith(IMergeable)}
		return true;
	}

	@Override
	public Object hackValue() {
		return null;
	}

	@Override
	public String exportToWeka() {
		return "";
	}

	@Override
	public String toDisplayName() {
		return "NONE";
	}

	@Override
	public NullValue clone() {
		try {
			return (NullValue) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}