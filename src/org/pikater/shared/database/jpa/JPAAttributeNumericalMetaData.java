package org.pikater.shared.database.jpa;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Class {@link JPAAttributeNumericalMetaData} represents a recourd about dataset's
 * one numerical attribute.
 */
@Entity
@Table(name = "AttributeNumericalMetaData")
@Inheritance(strategy = InheritanceType.JOINED)
public class JPAAttributeNumericalMetaData extends JPAAttributeMetaData {

	private boolean isReal;
	private double min;
	private double max;
	private double mode;
	private double median;
	private double variance;
	private double stddeviation;
	private double avarage;
	private double mean;
	private double q1;
	private double q2;
	private double q3;
	private double chiSquareNormalD;
	private double chiSquareTestNormalD;
	private double gTestNormalD;

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
	
	public void setStandardDeviation(double standardDeviation) {
		this.stddeviation=standardDeviation;
	}
	
	public double getStandardDeviation() {
		return stddeviation;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getQ1() {
		return q1;
	}

	public void setQ1(double q1) {
		this.q1 = q1;
	}

	public double getQ2() {
		return q2;
	}

	public void setQ2(double q2) {
		this.q2 = q2;
	}

	public double getQ3() {
		return q3;
	}

	public void setQ3(double q3) {
		this.q3 = q3;
	}

	public double getChiSquareNormalD() {
		return chiSquareNormalD;
	}

	public void setChiSquareNormalD(double chiSquareNormalD) {
		this.chiSquareNormalD = chiSquareNormalD;
	}

	public double getChiSquareTestNormalD() {
		return chiSquareTestNormalD;
	}

	public void setChiSquareTestNormalD(double chiSquareTestNormalD) {
		this.chiSquareTestNormalD = chiSquareTestNormalD;
	}

	public double getgTestNormalD() {
		return gTestNormalD;
	}

	public void setgTestNormalD(double gTestNormalD) {
		this.gTestNormalD = gTestNormalD;
	}

	@Transient
	public static final String EntityName = "AttributeNumericalMetaData";

}
