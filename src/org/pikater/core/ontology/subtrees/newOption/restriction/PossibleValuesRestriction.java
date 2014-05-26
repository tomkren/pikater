package org.pikater.core.ontology.subtrees.newOption.restriction;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.type.Type;

public class PossibleValuesRestriction implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -747009547261321953L;
	
	private List<List<Type>> possibleTypes;
	
	
	public PossibleValuesRestriction() {}
	public PossibleValuesRestriction(List<List<Type>> possibleTypes) {
		this.possibleTypes = possibleTypes;
	}

	public List<List<Type>> getPossibleTypes() {
		return possibleTypes;
	}
	public void setPossibleTypes(List<List<Type>> possibleTypes) {
		this.possibleTypes = possibleTypes;
	}
	public void addPossibleValues(List<Type> possibleTypes) {

		if (this.possibleTypes == null) {
			this.possibleTypes = new ArrayList<List<Type>>();
		}

		this.possibleTypes.add(possibleTypes);
	}

	@Override
	public Type getClassName() {

		if (possibleTypes == null || possibleTypes.isEmpty()) {
			return null;
		}

		Type type0 = possibleTypes.get(0).get(0);
		for (List<Type> typesI : possibleTypes) {
			for (Type typeJ : typesI) {

				if (typeJ.equals(type0)) {
					return null;
				}
			}
		}

		return type0;
	}

	@Override
	public boolean isValid() {

		if (possibleTypes == null || possibleTypes.isEmpty()) {
			return false;
		}

		return true;
	}

}
