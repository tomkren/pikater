package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.pikater.shared.database.exceptions.NotUpdatableEntityException;

/**
 * Class {@link JPAAttributeMetaData} represents a record about dataset's attribute, containing
 * data about common attribute properties (e.g. whether the attribute is target or not).
 * <p>
 * From this class are inherited classes, that store more specific properties based upon attributes type.
 * 
 * @see org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData
 * @see org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData
 */
@Entity
@Table(name = "AttributeMetaData")
@Inheritance(strategy = InheritanceType.JOINED)
public class JPAAttributeMetaData extends JPAAbstractEntity implements Comparable<JPAAttributeMetaData> {
	private double ratioOfMissingValues;
	private boolean isTarget;
	private String name;
	private double classEntropy;
	private double entropy;
	private int attrOrder;
	private double mannWhitney;
	private double kolmogorovSmirnov;
	private double covariance;

	public double getRatioOfMissingValues() {
		return ratioOfMissingValues;
	}

	public void setRatioOfMissingValues(double ratioOfMissingValues) {
		this.ratioOfMissingValues = ratioOfMissingValues;
	}

	public boolean isTarget() {
		return isTarget;
	}

	public void setTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getClassEntropy() {
		return classEntropy;
	}

	public void setClassEntropy(double classEntropy) {
		this.classEntropy = classEntropy;
	}

	public int getOrder() {
		return attrOrder;
	}

	public void setOrder(int order) {
		this.attrOrder = order;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public double getMannWhitney() {
		return mannWhitney;
	}

	public void setMannWhitney(double mannWhitney) {
		this.mannWhitney = mannWhitney;
	}

	public double getKolmogorovSmirnov() {
		return kolmogorovSmirnov;
	}

	public void setKolmogorovSmirnov(double kolmogorovSmirnov) {
		this.kolmogorovSmirnov = kolmogorovSmirnov;
	}

	public double getCovariance() {
		return covariance;
	}

	public void setCovariance(double covariance) {
		this.covariance = covariance;
	}

	@Override
	public void updateValues(JPAAbstractEntity newValues) throws NotUpdatableEntityException {
		throw new NotUpdatableEntityException();
	}

	/**
	 * Tests whether two attribute metadata entries are compatible for visualisation
	 * <p>
	 * Please note, that even this function returns true, doesn't mean that the result of
	 * visualisation should be attractive. 
	 * @param metadata Object of metadata for which we determine compatibility
	 * @return true if the objects can be visually compared
	 */
	public boolean isVisuallyCompatible(JPAAttributeMetaData metadata) {
		if (metadata instanceof JPAAttributeNumericalMetaData) {
			//pair of numerical metadata is always compatible
			//numerical is not compatible with categorical
			return this instanceof JPAAttributeNumericalMetaData;
		} else if (metadata instanceof JPAAttributeCategoricalMetaData) {
			if (this instanceof JPAAttributeCategoricalMetaData) {
				//two categoricals are compatible if having same category numbers
				return ((JPAAttributeCategoricalMetaData) this).getNumberOfCategories() == ((JPAAttributeCategoricalMetaData) metadata).getNumberOfCategories();
			} else {
				//categorical is not compatible with other types (numerical)
				return false;
			}
		} else {
			return false;
		}
	}

	/*
	 * The code below is required in web:
	 */

	@Override
	public int compareTo(JPAAttributeMetaData o) {
		return getName().compareTo(o.getName());
	}

	//--------------------------------------------------------
	// INSTANCE COMPARING INTERFACE - generated with Eclipse

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attrOrder;
		long temp;
		temp = Double.doubleToLongBits(classEntropy);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(entropy);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (isTarget ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		temp = Double.doubleToLongBits(ratioOfMissingValues);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		JPAAttributeMetaData other = (JPAAttributeMetaData) obj;
		if (attrOrder != other.attrOrder) {
			return false;
		}
		if (Double.doubleToLongBits(classEntropy) != Double.doubleToLongBits(other.classEntropy)) {
			return false;
		}
		if (Double.doubleToLongBits(entropy) != Double.doubleToLongBits(other.entropy)) {
			return false;
		}
		if (isTarget != other.isTarget) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (Double.doubleToLongBits(ratioOfMissingValues) != Double.doubleToLongBits(other.ratioOfMissingValues)) {
			return false;
		}
		return true;
	}
}
