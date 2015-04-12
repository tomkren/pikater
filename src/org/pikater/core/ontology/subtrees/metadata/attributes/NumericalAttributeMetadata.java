/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.ontology.subtrees.metadata.attributes;

/**
 * 
 * @author Kuba
 */
public class NumericalAttributeMetadata extends AttributeMetadata {

	private static final long serialVersionUID = -4614350036978302414L;

	private double min;
	private double max;
	private double avg;
	private double median;
	private double standardDeviation;
	private double variation;
	private double mean;
	private double q1;
	private double q2;
	private double q3;

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMedian() {
		return median;
	}

	public void setMedian(double median) {
		this.median = median;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public void setVariation(double variation) {
		this.variation = variation;
	}
	
	public double getVariation() {
		return variation;
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

	@Override
	public String getType() {
		return "Numerical";
	}


}
