package org.pikater.core.options.xmlGenerators;

import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.options.OptionDefault;
import org.pikater.core.ontology.options.types.OptionInterval;
import org.pikater.core.ontology.options.types.OptionList;
import org.pikater.core.ontology.options.types.OptionValue;
import org.pikater.core.options.LogicalUnitDescription;

public class LinearRegression extends LogicalUnitDescription
{	
	
	public LinearRegression() {

		//"Linear regression"
		this.setOntology(ComputingAgent.class);
		
		OptionDefault optionS = new OptionDefault();
		optionS.setName("S");
		optionS.setDescription("");
		optionS.setValue(
				new OptionValue(new Integer(0)) );
		optionS.setInterval(
				new OptionInterval(new Integer(0), new Integer(2)) );
		optionS.setList(
				new OptionList() );


		OptionDefault optionC = new OptionDefault();
		optionC.setName("C");
		optionC.setDescription("");
		optionC.setValue(
				new OptionValue(new Boolean(true)) );
		optionC.setInterval(null);
		optionC.setList(null);

		
		OptionDefault optionR = new OptionDefault();
		optionR.setName("R");
		optionR.setDescription("");
		optionR.setValue(
				new OptionValue(new Float(0.00000001f)) );
		optionR.setInterval(
				new OptionInterval(new Float(0.0000000001f), new Float(0.0001f)) );
		optionR.setList(new OptionList() );

		this.addParameter(optionS);
		this.addParameter(optionC);
		this.addParameter(optionR);

	}

}
