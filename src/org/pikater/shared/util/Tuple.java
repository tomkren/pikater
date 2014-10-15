package org.pikater.shared.util;

/**
 * Standard tuple implementation. May be used in collections and sets.
 * 
 * @author SkyCrawl
 *
 */
public class Tuple<V1, V2> {
	private V1 value1;
	private V2 value2;

	public Tuple(V1 value1, V2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	public V1 getValue1() {
		return value1;
	}

	public void setValue1(V1 value1) {
		this.value1 = value1;
	}

	public V2 getValue2() {
		return value2;
	}

	public void setValue2(V2 value2) {
		this.value2 = value2;
	}

	//-----------------------------------------------------
	// COMPARISON INTERFACE GENERATED WITH ECLIPSE

	/*
	 * Allows the use of this type in collections like Set and Map.
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value1 == null) ? 0 : value1.hashCode());
		result = prime * result + ((value2 == null) ? 0 : value2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Tuple other = (Tuple) obj;
		if (value1 == null) {
			if (other.value1 != null)
				return false;
		} else if (!value1.equals(other.value1))
			return false;
		if (value2 == null) {
			if (other.value2 != null)
				return false;
		} else if (!value2.equals(other.value2))
			return false;
		return true;
	}
}
