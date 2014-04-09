package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JPAAttributeNumericalMetaData extends JPAAttributeMetaData{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	private boolean isReal;
	private double min;
	private double max;
	private double mode;
	private double median;
	private double classEntropy;
	private double variance;
	private double avarage;

	public int getId() {
		return id;
	}
	public boolean isReal() {
		return isReal;
	}
	public void setReal(boolean isReal) {
		this.isReal = isReal;
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
	public double getClassEntropy() {
		return classEntropy;
	}
	public void setClassEntropy(double classEntropy) {
		this.classEntropy = classEntropy;
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
	
	
}
