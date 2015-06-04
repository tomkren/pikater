package org.pikater.core.agents.experiment.dataprocessing;

import jade.content.Concept;
import jade.content.ContentException;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.Slot;
import org.pikater.core.ontology.subtrees.batchdescription.DataProcessing;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.Task.InOutType;
import org.pikater.core.ontology.subtrees.task.TaskOutput;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
	gets weka instances on inputs, merges them, returns them as one output

 */
public class Agent_VotingAggregator extends Agent_DataProcessing {

	private static final long serialVersionUID = 4679962419249103511L;

	// TODO read from options
	private int numberOfInputs = 3;

	@Override
	public List<Ontology> getOntologies() {
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(ExperimentOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		return ontologies;
	}

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

	@Override
	protected void setup() {
		super.setup();

		Ontology ontology = TaskOntology.getInstance();
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchOntology(ontology.getName()),
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

		addBehaviour(new AchieveREResponder(this, template) {
			private static final long serialVersionUID = 746138569142900592L;

			@Override
			protected ACLMessage prepareResultNotification(ACLMessage request,
					ACLMessage response) throws FailureException {
				try {
					Concept action = ((Action) (getContentManager()
							.extractContent(request))).getAction();
					if (action instanceof ExecuteTask) {
						return respondToExecute(request);
					} else {
						throw new RefuseException("Invalid action requested");
					}
				} catch (CodecException e) {
					logException("Unknown codec: ", e);
					throw new FailureException("Unknown codec: "
							+ e.getMessage());
				} catch (OntologyException e) {
					logException("Unknown ontology: ", e);
					throw new FailureException("Unknown ontology: "
							+ e.getMessage());
				} catch (ContentException e) {
					logException("Content problem: ", e);
					throw new FailureException("Content problem: "
							+ e.getMessage());
				} catch (FIPAException e) {
					logException("FIPA exception when performing task", e);
					throw new FailureException(
							"Failed because of another agent " + e.getMessage());
				}
			}
		});

	}

	private ACLMessage respondToExecute(ACLMessage request)
			throws ContentException, FIPAException {
		logInfo("got execute");
		final ACLMessage reply = request.createReply();
		final ExecuteTask content;
		Task performed;
		try {
			content = (ExecuteTask) ((Action) getContentManager().extractContent(request)).getAction();
			performed = performTask(content.getTask());
		} catch (CodecException e) {
			logException("Failed to extract task", e);
			throw e;
		} catch (OntologyException e) {
			logException("Failed to extract task", e);
			throw e;
		}

		Result result = new Result(content, performed);
		getContentManager().fillContent(reply, result);
		reply.setPerformative(ACLMessage.INFORM);

		logInfo("responding with finished task");
		return reply;
	}

	private List<DataInstances> getDataForTask(Task t) throws FIPAException {
		List<DataInstances> res = new ArrayList<DataInstances>();
		for (Data dataI : t.getDatas().getDatas()) {
			String fname = dataI.getInternalFileName();
			ACLMessage request = makeGetDataRequest(fname);
			logInfo("sending getData for " + fname);
			ACLMessage reply = FIPAService.doFipaRequestClient(this, request);
			res.add(processGetDataResponse(reply));
			logInfo("got data for " + fname);
		}
		return res;
	}

	private Task performTask(Task t) throws FIPAException {
		logInfo("getting data");

		List<DataInstances> weatherData = getDataForTask(t);
		logInfo("processing data");
		List<TaskOutput> outputs = processData(weatherData);
		logInfo("returning outputs");
		t.setOutput(outputs);
		return t;
	}

	private Instances mergeInputs(List<DataInstances> data) {
		Instances res = new Instances(data.get(0).toWekaInstances());

		List<DataInstances> mergedData = new ArrayList();

		// Instances res = new Instances(data.get(0).toWekaInstances());
		for (int in=1; in<data.size(); in++){
			for (int i=0; i< data.get(in).getInstances().size(); i++) {
				res.add(data.get(in).toWekaInstances().instance(i));
			}
		}

		return res;
	}

	private List<TaskOutput> processData(List<DataInstances> data) {
		List<TaskOutput> res = new ArrayList<TaskOutput>();
		Instances output = mergeInputs(data);

		res.add(makeOutput(output, "Output"));
		return res;
	}

	private TaskOutput makeOutput(Instances instances, String dataType) {
		TaskOutput res = new TaskOutput();
		res.setName(saveArff(instances));
		res.setType(InOutType.DATA);
		res.setDataType(dataType);
		return res;
	}

}
