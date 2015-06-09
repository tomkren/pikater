package org.pikater.core.agents.experiment.dataprocessing;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.Slot;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.task.TaskOutput;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
	gets weka instances on inputs, merges them, returns them as one output

 */
public class Agent_RomanovoU extends Agent_AbstractDataProcessing {

	private static final long serialVersionUID = 4679962419249103511L;

	// TODO read from options
	private int numberOfInputs = 3;

	@Override
	protected AgentInfo getAgentInfo() {
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(this.getClass());
		agentInfo.importOntologyClass(DataProcessing.class);

		agentInfo.setName("RomanovoU");
		agentInfo.setDescription("Merges all inputs into one output.");

		// get all inputs
		ArrayList inputs = new ArrayList();
		for (int i=0; i<numberOfInputs; i++){
			Slot input = new Slot("Input_"+Integer.toString(i),
					CoreConstant.SlotCategory.DATA_GENERAL, "Input "+Integer.toString(i)+".");
			inputs.add(input);
		}

		agentInfo.setInputSlots(inputs);

		Slot output = new Slot("Output",
				CoreConstant.SlotCategory.DATA_GENERAL, "Output.");

		agentInfo.setOutputSlots(Arrays.asList(output));

		return agentInfo;
	}

	private DataInstances mergeInputs(List<DataInstances> data) {
		DataInstances res = data.get(0);

		List<DataInstances> mergedData = new ArrayList<>();

		// Instances res = new Instances(data.get(0).toWekaInstances());
		for (int in=1; in<data.size(); in++){
			DataInstances d = data.get(in);
			res.getInstances().addAll(d.getInstances());
		}

		return res;
	}

	protected List<TaskOutput> processData(List<DataInstances> data) {
		List<TaskOutput> res = new ArrayList<TaskOutput>();
		DataInstances output = mergeInputs(data);

		res.add(makeOutput(output, "Output"));
		return res;
	}


}
