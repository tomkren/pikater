package org.pikater.core.agents.experiment.dataprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.Slot;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.task.*;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class Agent_WeatherSplitter extends Agent_AbstractDataProcessing {

	private static final long serialVersionUID = 4679962419249103511L;

	@Override
	protected AgentInfo getAgentInfo() {
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(this.getClass());
		agentInfo.importOntologyClass(DataProcessing.class);

		agentInfo.setName("WeatherSplitter");
		agentInfo.setDescription("Splits weather data by prediction.");

		Slot i1 = new Slot("firstInput",
				CoreConstant.SlotCategory.DATA_GENERAL, "First weather input.");
		Slot i2 = new Slot("secondInput",
				CoreConstant.SlotCategory.DATA_GENERAL, "Second weather input.");

		agentInfo.setInputSlots(Arrays.asList(i1, i2));

		Slot sunny = new Slot("sunnyOutput",
				CoreConstant.SlotCategory.DATA_GENERAL, "Sunny output.");
		Slot overcast = new Slot("overcastOutput",
				CoreConstant.SlotCategory.DATA_GENERAL, "Overcast output.");
		Slot rainy = new Slot("rainyOutput",
				CoreConstant.SlotCategory.DATA_GENERAL, "Rainy output.");

		agentInfo.setOutputSlots(Arrays.asList(sunny, overcast, rainy));

		return agentInfo;
	}


	private Instances mergeInputs(List<DataInstances> weatherData) {
		Instances res = new Instances(weatherData.get(0).toWekaInstances());
		Instances second = weatherData.get(1).toWekaInstances();
		for (int i = 0; i < second.numInstances(); ++i) {
			res.add(second.instance(i));
		}
		return res;
	}

	protected List<TaskOutput> processData(List<DataInstances> weatherData) {
		List<TaskOutput> res = new ArrayList<TaskOutput>();
		Instances input = mergeInputs(weatherData);

		Instances sunny = new Instances(input, 0);
		Instances overcast = new Instances(input, 0);
		Instances rainy = new Instances(input, 0);

		Attribute forecast = input.attribute(0);

		for (int i = 0; i < input.numInstances(); ++i) {
			Instance instance = input.instance(i);
			String value = instance.stringValue(forecast);
			switch (value) {
				case "rainy":
					rainy.add(instance);
					break;
				case "overcast":
					overcast.add(instance);
					break;
				case "sunny":
					sunny.add(instance);
					break;
				default:
					throw new IllegalArgumentException("Unknown weather data: "
							+ value);
			}
		}

		res.add(makeOutput(sunny, "sunnyOutput"));
		res.add(makeOutput(overcast, "overcastOutput"));
		res.add(makeOutput(rainy, "rainyOutput"));
		return res;
	}

}
