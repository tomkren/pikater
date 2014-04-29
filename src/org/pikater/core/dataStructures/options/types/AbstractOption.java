package org.pikater.core.dataStructures.options.types;

import org.pikater.core.ontology.messages.Option;

public abstract class AbstractOption {

	public abstract Class<? extends Object> getOptionClass();
	
	public abstract Option toOption();

}
