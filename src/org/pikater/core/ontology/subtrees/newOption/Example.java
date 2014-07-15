package org.pikater.core.ontology.subtrees.newOption;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class Example {

	public static NewOption getU() {
		
		/*
			OptionDefault optionU = new OptionDefault();
			optionU.setName("U");
			optionU.setDescription("Use unpruned tree");
			optionU.setValue(
					new OptionValue(new Boolean(false)) );
		 */
		
		NewOption optionU = new NewOption("U", new BooleanValue(false));
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

		NewOption optionM = new NewOption("M", new IntegerValue(2), new RangeRestriction(new IntegerValue(1), new IntegerValue(10)));
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
		
		List<IValueData> values = new ArrayList<IValueData>();
		values.add(new NullValue());
		values.add(new NullValue());
		values.add(new NullValue());
		values.add(new FloatValue(0.0001f));
		values.add(new FloatValue(0.1f));
		values.add(new FloatValue(0.2f));
		values.add(new FloatValue(0.25f));
		values.add(new FloatValue(0.3f));
		values.add(new FloatValue(0.4f));
		
		NewOption optionC = new NewOption("C", new FloatValue(0.25f), new SetRestriction(values)); 
		optionC.setDescription("Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).");
		
		return optionC;
	}
}
