package org.pikater.core.agents.experiment.dataprocessing;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.Slot;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.task.TaskOutput;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Agent_KMeans extends Agent_AbstractDataProcessing {

	// TODO nacist z nastaveni
	private int k = 3;

	@Override
	protected AgentInfo getAgentInfo() {
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(this.getClass());
		agentInfo.importOntologyClass(DataProcessing.class);

		agentInfo.setName("K-Means");
		agentInfo.setDescription("Weka Simple K-Means algorithm.");

		Slot i1 = new Slot("Input",
				CoreConstant.SlotCategory.DATA_GENERAL, "First weather input.");

		agentInfo.setInputSlots(Arrays.asList(i1));


		ArrayList outputSlots = new ArrayList();
		for (int i=0; i< k; i++){
			Slot slot = new Slot("Output_"+Integer.toString(i),
					CoreConstant.SlotCategory.DATA_GENERAL, "Output "+Integer.toString(i)+".");
			outputSlots.add(slot);
		}

		agentInfo.setOutputSlots(outputSlots);

		return agentInfo;
	}

	@Override
	protected List<TaskOutput> processData(List<DataInstances> data) {
		List<TaskOutput> res = new ArrayList<TaskOutput>();
		Instances input = data.get(0).toWekaInstances(DataInstances.Export.PUBLIC_WITHOUT_CLASS);

		// create the model

		SimpleKMeans kMeans = new SimpleKMeans();
		try {
			kMeans.setPreserveInstancesOrder(true);
			kMeans.setNumClusters(k);
			kMeans.buildClusterer(input);

			int[] assignments = kMeans.getAssignments();

			// create arraylist of cluster clustered[cluster_number][instance]
			ArrayList<DataInstances> clustered = new ArrayList<>();

			for (int i=0; i < k; i++){
				clustered.add(data.get(0).createEmptyCopy());
			}

			for (int i=0; i<input.numInstances(); i++){
				clustered.get(assignments[i]).add(data.get(0).getInstances().get(i));
			}

			// create outputs
			for (int i=0; i < k; i++){
				res.add(makeOutput((clustered.get(i)), "Output_"+Integer.toString(i)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

}
