package org.pikater.core.options.xmlGenerators;

import org.pikater.shared.experiment.resources.ParamResource;

public class J48_MethodBoxResources {
	
	public static final ParamResource u = new ParamResource("U", "Use unpruned tree (default false)");
	public static final ParamResource c = new ParamResource("C", "Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).");
	public static final ParamResource m = new ParamResource("M", "Set minimum number of instances per leaf. (Default: 2)");
	public static final ParamResource r = new ParamResource("R", "Use reduced error pruning. No subtree raising is performed.");
    public static final ParamResource n = new ParamResource("N", "Set number of folds for reduced error pruning. One fold is used as the pruning set. (Default: 3)");
	public static final ParamResource b = new ParamResource("B", "Use binary splits for nominal attributes.");
	public static final ParamResource s = new ParamResource("S", "Don't perform subtree raising.");
    public static final ParamResource a = new ParamResource("A", "If set, Laplace smoothing is used for predicted probabilites.");
	public static final ParamResource q = new ParamResource("Q", "The seed for reduced-error pruning. (default 1)");
}
