package org.pikater.core.ontology.subtrees.newoption.values;

import java.util.List;

import org.pikater.core.ontology.subtrees.newoption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValidatedValueData;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;

public class QuestionMarkSet implements IValidatedValueData {
	private static final long serialVersionUID = 4434448192843651477L;

	private int countOfValuesToTry;
	private SetRestriction userDefinedRestriction;

	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public QuestionMarkSet() {
	}

	/**
	 * Main constructor.
	 */
	public QuestionMarkSet(int countOfValuesToTry, List<IValueData> values) {
		this(countOfValuesToTry, new SetRestriction(false, values));
	}

	/**
	 * More or less a copy constructor. For internal use only.
	 */
	private QuestionMarkSet(int countOfValuesToTry, SetRestriction restriction) {
		this.countOfValuesToTry = countOfValuesToTry;
		this.userDefinedRestriction = restriction;
	}

	public int getCountOfValuesToTry() {
		return countOfValuesToTry;
	}

	public void setCountOfValuesToTry(int countOfValuesToTry) {
		this.countOfValuesToTry = countOfValuesToTry;
	}

	public SetRestriction getUserDefinedRestriction() {
		return userDefinedRestriction;
	}

	public void setUserDefinedRestriction(SetRestriction userDefinedRestriction) {
		this.userDefinedRestriction = userDefinedRestriction;
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
		result = prime * result + countOfValuesToTry;
		result = prime
				* result
				+ ((userDefinedRestriction == null) ? 0
						: userDefinedRestriction.hashCode());
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
		QuestionMarkSet other = (QuestionMarkSet) obj;
		if (countOfValuesToTry != other.countOfValuesToTry) {
			return false;
		}
		if (userDefinedRestriction == null) {
			if (other.userDefinedRestriction != null) {
				return false;
			}
		} else if (!userDefinedRestriction.equals(other.userDefinedRestriction)) {
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	@Override
	public Object hackValue() {
		return null;
	}

	@Override
	public QuestionMarkSet clone() {
		QuestionMarkSet result;
		try {
			result = (QuestionMarkSet) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		result.setCountOfValuesToTry(countOfValuesToTry);
		result.setUserDefinedRestriction(userDefinedRestriction.clone());
		return result;
	}

	@Override
	public String exportToWeka() {
		return "?";
	}

	@Override
	public String toDisplayName() {
		return "QuestionMarkSet";
	}

	@Override
	public boolean isValid() {
		return (userDefinedRestriction != null)
				&& userDefinedRestriction.isValid()
				&& (countOfValuesToTry > 0)
				&& (countOfValuesToTry <= userDefinedRestriction.getValues()
						.size());
	}
}