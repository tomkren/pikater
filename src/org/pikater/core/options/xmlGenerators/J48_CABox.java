package org.pikater.core.options.xmlGenerators;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.options.LogicalBoxDescription;

public class J48_CABox extends LogicalBoxDescription {
	protected J48_CABox() {
		super("J48",
				ComputingAgent.class,
				"J48 method description");

		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
				
		OptionDefault optionU = new OptionDefault();
		optionU.setName("U");
		optionU.setDescription("Use unpruned tree");
		optionU.setValue(
				new OptionValue(new Boolean(false)) );
		

		OptionDefault optionC = new OptionDefault();
		optionC.setName("C");
		optionC.setDescription("Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).");
		optionC.setValue(
				new OptionValue(new Float(0.25f)) );
		optionC.setInterval( null );
		OptionList listC = new OptionList();
		listC.setList(Arrays.asList(new Object[] {null,null,null,0.0001f,0.1f,0.2f,0.25f,0.3f,0.4f}));
		optionC.setList( listC );


		OptionDefault optionM = new OptionDefault();
		optionM.setName("M");
		optionM.setDescription("Set minimum number of instances per leaf");
		optionM.setValue(
				new OptionValue(new Integer(2)) );
		optionM.setInterval(
				new OptionInterval(new Integer(1), new Integer(10)) );
		optionM.setList( new OptionList() );
		
		
		OptionDefault optionR = new OptionDefault();
		optionR.setName("R");
		optionR.setDescription("Use reduced error pruning. No subtree raising is performed");
		optionR.setValue(
				new OptionValue(new Boolean(false)) );

		OptionDefault optionN = new OptionDefault();
		optionN.setName("N");
		optionN.setDescription("Set number of folds for reduced error pruning. One fold is used as the pruning set");
		optionN.setValue(
				new OptionValue(new Integer(3)) );
		optionN.setInterval(
				new OptionInterval(new Integer(1), new Integer(10)) );
		OptionList listN = new OptionList();
		listN.setList(Arrays.asList(new Object[] {null,1,2,3,4,5}) );
		optionN.setList( listN );

		
		OptionDefault optionB = new OptionDefault();
		optionB.setName("B");
		optionB.setDescription("Use binary splits for nominal attributes");
		optionB.setValue(
				new OptionValue(new Boolean(false)) );

		OptionDefault optionS = new OptionDefault();
		optionS.setName("S");
		optionS.setDescription("Don't perform subtree raising");
		optionS.setValue(
				new OptionValue(new Boolean(false)) );
		
		OptionDefault optionA = new OptionDefault();
		optionA.setName("A");
		optionA.setDescription("If set, Laplace smoothing is used for predicted probabilites");
		optionA.setValue(
				new OptionValue(new Boolean(false)) );

		OptionDefault optionQ = new OptionDefault();
		optionQ.setName("Q");
		optionQ.setDescription("The seed for reduced-error pruning");
		optionQ.setValue(
				new OptionValue(new Integer(1)) );
		optionQ.setInterval(
				new OptionInterval(new Integer(1), new Integer(Integer.MAX_VALUE)) );

		
		this.addParameter(optionU);
		this.addParameter(optionC);
		this.addParameter(optionM);
		this.addParameter(optionR);
		this.addParameter(optionN);
		this.addParameter(optionB);
		this.addParameter(optionS);
		this.addParameter(optionA);
		this.addParameter(optionQ);
	}
}
