package org.pikater.web.visualisation.charts.coloring;

import java.awt.Color;

import org.pikater.web.visualisation.charts.coloring.exception.ColorerNotMergeableException;

public class LinearColorer implements Colorer {

	private double min;
	private double max;
	
	public LinearColorer(){
		this.min=0.0;
		this.max=10.0;
	}
	
	public LinearColorer(double min,double max){
		this.min=min;
		this.max=max;
	}
	
	/**
	 * it change the saturation fo color based on the value
	 * if the value is Double.NaN then returns Color.BLACK
	 * if the value is Infinite of Double the returns Color.GRAY
	 */
	@Override
	public Color getColor(double value) {
		if(Double.isNaN(value))
			return Color.BLACK;
		if(Double.isInfinite(value))
			return Color.GRAY;
		float saturation = (float) (0.0 + ((value-min)/(max-min))*1.0);
		return Color.getHSBColor(1.0f, saturation, 1.0f);
	}

	@Override
	public Colorer merge(Colorer colorer) throws ColorerNotMergeableException {
		if(colorer instanceof LinearColorer){
			LinearColorer linCol=(LinearColorer)colorer;
			if(linCol.min<this.min){
				this.min=linCol.min;
			}
			
			if(linCol.max>this.max){
				this.max=linCol.max;
			}
			
			return this;
		}else{
			throw new ColorerNotMergeableException();
		}
	}

}
