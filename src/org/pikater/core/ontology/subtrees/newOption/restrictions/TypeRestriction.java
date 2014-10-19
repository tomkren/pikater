package org.pikater.core.ontology.subtrees.newoption.restrictions;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newoption.base.ValueType;
import org.pikater.shared.util.collections.CollectionUtils;

public class TypeRestriction implements IRestriction {
	private static final long serialVersionUID = -135700897678377163L;

	private List<ValueType> types;

	/**
	 * Should only be used internally and by Jade.
	 */
	@Deprecated
	public TypeRestriction() {
		this.types = new ArrayList<ValueType>();
	}

	public TypeRestriction(List<ValueType> types) {
		this();
		this.types.addAll(types);
	}

	public List<ValueType> getTypes() {
		return types;
	}

	public void setTypes(List<ValueType> types) {
		this.types.clear();
		this.types.addAll(types);
	}

	/*
	 * Some convenience interface.
	 */
	public void addType(ValueType type) {
		types.add(type);
	}

	public boolean isValidAgainst(Object obj) {
		return isValid() && (obj instanceof ValueType)
				&& types.contains((ValueType) obj);
	}

	/*
	 * Inherited interface.
	 */
	@Override
	public TypeRestriction clone() {
		TypeRestriction result;
		try {
			result = (TypeRestriction) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		result.setTypes(CollectionUtils.deepCopy(types));
		return result;
	}

	@Override
	public boolean isValid() {
		return (types != null) && !types.isEmpty();
	}
}