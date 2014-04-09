package org.pikater.core.options.xmlGenerators;

import org.pikater.shared.experiment.resources.ParamResource;

public class GASearchBoxResources {
	
	public static final ParamResource e = new ParamResource("E", "Minimum error rate (default 0.1)");
	public static final ParamResource m = new ParamResource("M", "Maximal number of generations (default 10)");
	public static final ParamResource t = new ParamResource("T", "Mutation rate (default 0.2)");
	public static final ParamResource x = new ParamResource("X", "Crossover probability (default 0.5)");
	public static final ParamResource p = new ParamResource("P", "Population size (default 5)");
	public static final ParamResource s = new ParamResource("S", "Size of tournament in selection (default 2)");
	
}
