package org.pikater.core.ontology.subtrees.newOption.restriction;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;

public class TypeRestriction implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -747009547261321953L;
	
	private List<Types> possibleTypes;
	
	
	public TypeRestriction() {}
	public TypeRestriction(List<Types> possibleTypes) {
		this.possibleTypes = possibleTypes;
	}

	public List<Types> getPossibleTypes() {
		return possibleTypes;
	}
	public void setPossibleTypes(List<Types> possibleTypes) {
		this.possibleTypes = possibleTypes;
	}
	public void addPossibleValues(Types types) {

		if (this.possibleTypes == null) {
			this.possibleTypes = new ArrayList<Types>();
		}

		this.possibleTypes.add(types);
	}
	public void addPossibleValues(Type type, int minCount, int maxCount) {
		
		if (type == null) {
			throw new IllegalArgumentException("Argument type is null");
		}

		if (minCount < 0 || maxCount <= 0 || minCount > maxCount) {
			throw new IllegalArgumentException("Arguments minCount and maxCount represents incorrect interval");
		}
		
		for (int typeCountI = minCount; typeCountI <= maxCount; typeCountI++) {
			
			List<Type> typeList = new ArrayList<Type>();
			for (int typeIndex = 1; typeIndex <= typeCountI; typeIndex++) {
				typeList.add(type);
			}
			addPossibleValues(new Types(typeList) );
			
		}
	}
	
	@Override
	public Type getType() {

		if (possibleTypes == null || possibleTypes.isEmpty()) {
			return null;
		}

		Type type0 = possibleTypes.get(0).getTypes().get(0);
		for (Types typesI : possibleTypes) {
			for (Type typeJ : typesI.getTypes()) {

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

	@Override
	public TypeRestriction clone() {
		
		TypeRestriction ptr = new TypeRestriction();
		for (Types typesI : possibleTypes) {
			ptr.addPossibleValues(typesI.cloneTypes());
		}
		return ptr;
	}
}
