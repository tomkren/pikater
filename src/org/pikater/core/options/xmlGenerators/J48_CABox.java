package org.pikater.core.options.xmlGenerators;

import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaJ48;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.ontology.description.ComputingAgent;

public class J48_CABox {
	
	public static AgentInfo get() {
		
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

		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaJ48.class.getName());
		agentInfo.setOntologyClass(ComputingAgent.class.getName());
	
		agentInfo.setName("J48");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("J48 method description");

		agentInfo.addOption(optionU.toOption());
		agentInfo.addOption(optionC.toOption());
		agentInfo.addOption(optionM.toOption());
		agentInfo.addOption(optionR.toOption());
		agentInfo.addOption(optionN.toOption());
		agentInfo.addOption(optionB.toOption());
		agentInfo.addOption(optionS.toOption());
		agentInfo.addOption(optionA.toOption());
		agentInfo.addOption(optionQ.toOption());


		//Slot Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
