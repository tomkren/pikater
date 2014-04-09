package org.pikater.core.options.xmlGenerators;

import org.pikater.shared.experiment.resources.ParamResource;

public class EASearchBoxResources {
	public static final ParamResource e = new ParamResource("E", "Minimum error rate (default 0.1)");
	public static final ParamResource m = new ParamResource("M", "Maximal number of generations (default 10)");
	public static final ParamResource t = new ParamResource("T", "Mutation rate (default 0.2)");
	public static final ParamResource x = new ParamResource("X", "Crossover probability (default 0.5)");
	public static final ParamResource p = new ParamResource("P", "Population size (default 10)");
	public static final ParamResource i = new ParamResource("I", "Maximum number of option evaluations (default 100)");
	public static final ParamResource f = new ParamResource("F", "Mutation rate per field in individual (default 0.2)");
	public static final ParamResource l = new ParamResource("L", "The percentage of elite individuals (default 0.1)");
}
