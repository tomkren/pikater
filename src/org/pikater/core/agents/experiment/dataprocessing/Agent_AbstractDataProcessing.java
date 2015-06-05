package org.pikater.core.agents.experiment.dataprocessing;

import jade.content.Concept;
import jade.content.ContentException;
import jade.content.lang.Codec;
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
import org.pikater.core.agents.experiment.Agent_DataProcessing;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ExperimentOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.datainstance.DataInstances;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.TaskOutput;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 4.6.2015.
 */
public abstract class Agent_AbstractDataProcessing extends Agent_DataProcessing {
	private static final long serialVersionUID = 4679962419249103511L;

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
	protected abstract AgentInfo getAgentInfo();

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
				} catch (Codec.CodecException e) {
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
			content = (ExecuteTask) ((Action) getContentManager()
					.extractContent(request)).getAction();
			performed = performTask(content.getTask());
		} catch (Codec.CodecException e) {
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

		List<DataInstances> data = getDataForTask(t);
		logInfo("processing data");
		List<TaskOutput> outputs = processData(data);
		logInfo("returning outputs");
		t.setOutput(outputs);
		return t;
	}

	protected abstract List<TaskOutput> processData(List<DataInstances> data);

	protected TaskOutput makeOutput(DataInstances dataInstances, String dataType) {
		Instances instances = dataInstances.toWekaInstances(true);
		TaskOutput res = new TaskOutput();
		res.setName(saveArff(instances));
		res.setType(Task.InOutType.DATA);
		res.setDataType(dataType);
		return res;
	}
}
