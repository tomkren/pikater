package org.pikater.core.ontology.subtrees.newOption.example;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.value.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.value.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.value.NullValue;

public class Exmple {

	public static NewOption getU() {
		
		/*
			OptionDefault optionU = new OptionDefault();
			optionU.setName("U");
			optionU.setDescription("Use unpruned tree");
			optionU.setValue(
					new OptionValue(new Boolean(false)) );
		 */
		
		NewOption optionU = new NewOption(new BooleanValue(false), "U");
		optionU.setDescription("Use unpruned tree");
		
		return optionU;
	}
	
	public static NewOption getN() {
		
		/* 
			OptionDefault optionM = new OptionDefault();
			optionM.setName("M");
			optionM.setDescription("Set minimum number of instances per leaf");
			optionM.setValue(
					new OptionValue(new Integer(2)) );
			optionM.setInterval(
					new OptionInterval(new Integer(1), new Integer(10)) );
			optionM.setList( new OptionList() );
		 */

		Type typeM = new Type(
				IntegerValue.class,
				new RangeRestriction(new IntegerValue(1), new IntegerValue(10))
				);
		
		NewOption optionM = new NewOption(
				new IntegerValue(2),
				"M");
		optionM.setDescription("Set minimum number of instances per leaf");
		
		return optionM;
	}
	
	public static NewOption getC() {
		/*
		 * J48_CABox

			OptionDefault optionC = new OptionDefault();
			optionC.setName("C");
			optionC.setDescription("Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).");
			optionC.setValue(
					new OptionValue(new Float(0.25f)) );
			optionC.setInterval( null );
			OptionList listC = new OptionList();
			listC.setList(Arrays.asList(new Object[] {null,null,null,0.0001f,0.1f,0.2f,0.25f,0.3f,0.4f}));
			optionC.setList( listC );
		*/
		
		List<ITypedValue> values = new ArrayList<ITypedValue>();
		values.add(new NullValue());
		values.add(new NullValue());
		values.add(new NullValue());
		values.add(new FloatValue(0.0001f));
		values.add(new FloatValue(0.1f));
		values.add(new FloatValue(0.2f));
		values.add(new FloatValue(0.25f));
		values.add(new FloatValue(0.3f));
		values.add(new FloatValue(0.4f));
		
		Type typeC = new Type(
				FloatValue.class,
				new SetRestriction(values)
				);
		
		NewOption optionC = new NewOption(new FloatValue(0.25f), typeC, "C");
		optionC.setDescription("Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).");
		
		return optionC;
	}
}
