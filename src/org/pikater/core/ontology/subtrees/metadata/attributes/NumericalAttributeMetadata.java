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

	@Override
	public String getType() {
		return "Numerical";
	}

}
