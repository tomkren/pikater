package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="AttributeNumericalMetaData")
@Inheritance(strategy=InheritanceType.JOINED)
public class JPAAttributeNumericalMetaData extends JPAAttributeMetaData{
	
	private boolean isReal;
	private double min;
	private double max;
	private double mode;
	private double median;
	private double variance;
	private double avarage;

	public void setReal(boolean isReal) {
		this.isReal = isReal;
	}
	public boolean getIsReal() {
		return this.isReal;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getMode() {
		return mode;
	}
	public void setMode(double mode) {
		this.mode = mode;
	}
	public double getMedian() {
		return median;
	}
	public void setMedian(double median) {
		this.median = median;
	}
	public double getVariance() {
		return variance;
	}
	public void setVariance(double variance) {
		this.variance = variance;
	}
	public double getAvarage() {
		return avarage;
	}
	public void setAvarage(double avarage) {
		this.avarage = avarage;
	}
	@Transient
	public static final String EntityName = "AttributeNumericalMetaData";
	
	
}
