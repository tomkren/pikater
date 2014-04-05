package pikater.utility.experiment.boxes.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import pikater.utility.experiment.parameters.EnumeratedValueParameter;
import pikater.utility.experiment.parameters.RangedValueParameter;
import pikater.utility.experiment.parameters.ValueParameter;
import pikater.utility.experiment.parameters.info.ParamInfo;
import pikater.utility.experiment.util.Interval;

public class LinearRegressionBox extends LeafBox {
	private EnumeratedValueParameter<Integer> attributeSelection = new EnumeratedValueParameter<Integer>(0,new ArrayList<Integer>(Arrays.asList(new Integer[] {0,1,2})));
	private ValueParameter<Boolean> eliminateColinearAttributtes=new ValueParameter<Boolean>(false);
	/**
	 * NOTE: I haven't found in the .opt file whether the parameter is strict or not, so I set it to non-strict
	 */
	private RangedValueParameter<Float> ridge=new RangedValueParameter<Float>((float)1.0e-08, new Interval<Float>(0.0000000001f, 0.0001f), false);
	
	
	
	public LinearRegressionBox(String displayName, String picture,
			String description, String agentClassName, String ontologyClassName) {
		super(displayName, picture, description, agentClassName, ontologyClassName);
		
		this.addEditableParameter(new ParamInfo("lin_reg_box_attr_sel", "Attribute Selection", "Set the attriute selection method to use. 1 = None, 2 = Greedy (default 0 = M5' method)"), attributeSelection);
		this.addEditableParameter(new ParamInfo("lin_reg_box_elim_colin_attr", "Eliminate Colinear Attributes", "Try to eliminate colinear attributes"), eliminateColinearAttributtes);
		this.addEditableParameter(new ParamInfo("lin_reg_box_ridge", "Ridge Parameter", "The ridge parameter (default 1.0e-8)"), ridge);
	}
	@Override
	public Set<LeafBoxCategory> getCategories() {
		return null;
	}

}
