package org.pikater.core.options;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaJ48;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class J48_CABox {
	
	public static AgentInfo get() {
		
		NewOption optionU = new NewOption("U", new BooleanValue(false));
		optionU.setDescription("Use unpruned tree");
		
		
		// TODO: why all these null values if the collection is used as a set restriction?
		List<IValueData> valuesC = new ArrayList<IValueData>();
		valuesC.add(new NullValue());
		valuesC.add(new NullValue());
		valuesC.add(new NullValue());
		valuesC.add(new FloatValue(0.0001f));
		valuesC.add(new FloatValue(0.1f));
		valuesC.add(new FloatValue(0.2f));
		valuesC.add(new FloatValue(0.25f));
		valuesC.add(new FloatValue(0.3f));
		valuesC.add(new FloatValue(0.4f));
				
		NewOption optionC = new NewOption("C", new FloatValue(0.25f), new SetRestriction(valuesC));
		optionC.setDescription("Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).");
		
		
		NewOption optionM = new NewOption("M", new IntegerValue(2), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(10))
		);
		optionM.setDescription("Set minimum number of instances per leaf");
		
				
		NewOption optionR = new NewOption("R", new BooleanValue(false));
		optionR.setDescription("Use reduced error pruning. No subtree raising is performed");

		
		NewOption optionN = new NewOption("N", new IntegerValue(3), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(10))
		);
		optionN.setDescription("Set minimum number of instances per leaf");

		
		NewOption optionB = new NewOption("B", new BooleanValue(false)); 
		optionB.setDescription("Use binary splits for nominal attributes");
		

		NewOption optionS = new NewOption("S", new BooleanValue(false));
		optionS.setDescription("Don't perform subtree raising");
		
		
		NewOption optionA = new NewOption("A", new BooleanValue(false));
		optionA.setDescription("If set, Laplace smoothing is used for predicted probabilites");


		NewOption optionQ = new NewOption("Q", new IntegerValue(3), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(Integer.MAX_VALUE))
		);
		optionQ.setDescription("The seed for reduced-error pruning");
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaJ48.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("J48");
		agentInfo.setDescription("J48 method description");

		agentInfo.addOption(optionU);
		agentInfo.addOption(optionC);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionR);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionB);
		agentInfo.addOption(optionS);
		agentInfo.addOption(optionA);
		agentInfo.addOption(optionQ);


		//Slot Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
