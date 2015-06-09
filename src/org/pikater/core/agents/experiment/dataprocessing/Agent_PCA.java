package org.pikater.core.agents.experiment.dataprocessing;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.Slot;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.task.TaskOutput;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Agent_PCA extends Agent_AbstractDataProcessing {

	private static final long serialVersionUID = 4679962419249103511L;
	private int maxiumAttributes = 5;

	@Override
	protected AgentInfo getAgentInfo() {
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(this.getClass());
		agentInfo.importOntologyClass(DataProcessing.class);

		agentInfo.setName("PCA");
		agentInfo.setDescription("Weka Principal Component Analysis.");

		Slot i1 = new Slot("Input",
				CoreConstant.SlotCategory.DATA_GENERAL, "Input data.");

		agentInfo.setInputSlots(Arrays.asList(i1));

		Slot o1 = new Slot("Output",
				CoreConstant.SlotCategory.DATA_GENERAL, "Transformed data.");

		agentInfo.setOutputSlots(Arrays.asList(o1));

		return agentInfo;
	}

	protected List<TaskOutput> processData(List<DataInstances> data) {
		List<TaskOutput> res = new ArrayList<TaskOutput>();
		DataInstances dinst = data.get(0);
		Instances input = dinst.toWekaInstances();

		PrincipalComponents pca = new PrincipalComponents();

		Instances output;

		try {
			pca.setInputFormat(input);
			pca.setMaximumAttributes(maxiumAttributes); // TODO nacist z nastaveni

			output = Filter.useFilter(input, pca);
			dinst.mergePublicData(output);
			res.add(makeOutput(dinst, "Output"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

}
