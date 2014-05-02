package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.pikater.shared.utilities.pikaterDatabase.newDB.exceptions.NotUpdatableEntityException;

@Entity
@Table(name="AttributeMetaData_20140430")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class JPAAttributeMetaData extends JPAAbstractEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int getId() {
        return id;
    }
	private double ratioOfMissingValues;
	private boolean isTarget;

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
	
	@Override
	public void updateValues(JPAAbstractEntity newValues) throws NotUpdatableEntityException{
		throw new NotUpdatableEntityException();
	}
}
