package org.pikater.core.dataStructures.options;

import org.pikater.core.dataStructures.options.types.AbstractOption;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.messages.option.Option;

public abstract class Converter {

	public static Option toOption(StepanuvOption sOption) {

		AbstractOption option = sOption.getOption();
		
		Option resultOption = null;
		if (option instanceof OptionInterval) {
			
			resultOption = Converter.toOption( (OptionInterval) option);
			
		} else if (option instanceof OptionInterval) {

			resultOption = Converter.toOption( (OptionList) option);

		} else if (option instanceof OptionValue) {
			
			resultOption = Converter.toOption( (OptionValue) option);

		} else {}
		
		resultOption.setName(sOption.getName());
		resultOption.setSynopsis(sOption.getSynopsis());
		
		return resultOption;
	}
	
	private static Option toOption(OptionInterval sInterval) {
		return null;
	}
	private static Option toOption(OptionList sInterval) {
		return null;
	}
	private static Option toOption(OptionValue sValue) {
		// TODO: Not complete

		Object object = sValue.getObject();
		Option option = new Option();
		
		if (object instanceof Integer) {
			
			Integer thisInteger = (Integer) object;
			option.setData_type("INT");
			option.setValue(String.valueOf(thisInteger));
			
		} else if (object instanceof Float) {
			
			Float thisFloat = (Float) object;
			option.setData_type("FLOAT");
			option.setValue(String.valueOf(thisFloat));
			
		} else if (object instanceof String) {
		
			String thisString = (String) object;
			option.setData_type("STRING");
			option.setValue(thisString);
		}

		return option;
	}
	
}
