package org.pikater.core.dataStructures.options.types;

import org.pikater.core.ontology.messages.Option;



public class OptionInterval extends AbstractOption {

	private Number minValue;
	private Number maxValue;

	public OptionInterval() {}

	public OptionInterval(Number minValue, Number maxValue) {
		
		this.minValue = minValue;
		this.maxValue = maxValue;
	}


	@Override
	public Class<? extends Object> getOptionClass() {
		
		if (minValue.getClass() == maxValue.getClass()) {
			
			return minValue.getClass();
			
		} else {
			return null;
		}
	}
	
	public Number getMinValue() {
		return minValue;
	}
	public void setMinValue(Number minValue) {
		this.minValue = minValue;
	}

	public Number getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Number maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public Option toOption() {
		// TODO Auto-generated method stub
		return null;
	}

}
