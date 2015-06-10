package org.pikater.core.agents.experiment.dataprocessing;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.Slot;
import org.pikater.core.ontology.subtrees.attribute.Attribute;
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

		Slot trainingSlot = new Slot(CoreConstant.SlotContent.TRAINING_DATA);
		Slot testingSlot = new Slot(CoreConstant.SlotContent.TESTING_DATA);
		Slot validationSlot = new Slot(CoreConstant.SlotContent.VALIDATION_DATA);
		Slot dataToLabelSlot = new Slot(CoreConstant.SlotContent.DATA_TO_LABEL);

		agentInfo.setInputSlots(Arrays.asList(trainingSlot, testingSlot, validationSlot, dataToLabelSlot));

		Slot o1 = new Slot(CoreConstant.SlotContent.TRAINING_DATA.getSlotName(), CoreConstant.SlotCategory.DATA_GENERAL);
		Slot o2 = new Slot(CoreConstant.SlotContent.TESTING_DATA.getSlotName(), CoreConstant.SlotCategory.DATA_GENERAL);
		Slot o3 = new Slot(CoreConstant.SlotContent.VALIDATION_DATA.getSlotName(), CoreConstant.SlotCategory.DATA_GENERAL);
		Slot o4 = new Slot(CoreConstant.SlotContent.DATA_TO_LABEL.getSlotName(), CoreConstant.SlotCategory.DATA_GENERAL);

		agentInfo.setOutputSlots(Arrays.asList(o1, o2, o3, o4));

		return agentInfo;
	}

	protected List<TaskOutput> processData(List<DataInstances> data) {

		List<TaskOutput> res = new ArrayList<TaskOutput>();
		DataInstances dinst = data.get(0);
		Instances input = dinst.toWekaInstances(DataInstances.Export.PUBLIC_WITHOUT_CLASS);

		PrincipalComponents pca = new PrincipalComponents();

		List<Double> classes = dinst.extractClassColumn();
		Attribute classAttr = dinst.getClassAttribute();
		Instances output;

		try {
			pca.setInputFormat(input);
			pca.setMaximumAttributes(maxiumAttributes);  // TODO z ontologii

			output = Filter.useFilter(input, pca);
			dinst.mergePublicData(output);
			dinst.appendClassColumn(classes, classAttr);
			res.add(makeOutput(dinst, CoreConstant.SlotContent.TRAINING_DATA.getSlotName()));

			dinst = data.get(1);
			input = dinst.toWekaInstances(DataInstances.Export.PUBLIC_WITHOUT_CLASS);
			classes = dinst.extractClassColumn();
			classAttr = dinst.getClassAttribute();
			output = Filter.useFilter(input, pca);
			dinst.mergePublicData(output);
			dinst.appendClassColumn(classes, classAttr);
			res.add(makeOutput(dinst, CoreConstant.SlotContent.TESTING_DATA.getSlotName()));

			dinst = data.get(2);
			input = dinst.toWekaInstances(DataInstances.Export.PUBLIC_WITHOUT_CLASS);
			classes = dinst.extractClassColumn();
			classAttr = dinst.getClassAttribute();
			output = Filter.useFilter(input, pca);
			dinst.mergePublicData(output);
			dinst.appendClassColumn(classes, classAttr);
			res.add(makeOutput(dinst, CoreConstant.SlotContent.VALIDATION_DATA.getSlotName()));

			dinst = data.get(3);
			input = dinst.toWekaInstances(DataInstances.Export.PUBLIC_WITHOUT_CLASS);
			classes = dinst.extractClassColumn();
			classAttr = dinst.getClassAttribute();
			output = Filter.useFilter(input, pca);
			dinst.mergePublicData(output);
			dinst.appendClassColumn(classes, classAttr);
			res.add(makeOutput(dinst, CoreConstant.SlotContent.DATA_TO_LABEL.getSlotName()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

}
