package tests.pikater.core.agents.system;

import jade.content.AgentAction;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

public class WeatherSplitterTester extends PikaterAgent {
	private static final long serialVersionUID = 1677484717124329173L;

	@Override
	public List<Ontology> getOntologies() {
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(TaskOntology.getInstance());
		return ontologies;
	}

	@Override
	protected void setup() {
		initDefault();
		setEnabledO2ACommunication(true, 0);

		AID receiver = new AID("splitter", false);

		ExecuteTask ex = new ExecuteTask();
		Data data = new Data();
		ArrayList<String> inputs = new ArrayList<>();
		inputs.add("28c7b9febbecff6ce207bcde29fc0eb8");
		inputs.add("28c7b9febbecff6ce207bcde29fc0eb8"); // same file twice for testing
		data.setDataFileNames(inputs);
		Task task = new Task();
		task.setData(data);
		ex.setTask(task);
		
		try {
			ACLMessage request = makeActionRequest(receiver, ex);
			log("Sending test request");
			ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 10000);
			if (reply == null)
				logError("Reply not received.");
			else
				log("Reply received: " + ACLMessage.getPerformative(reply.getPerformative()) + " " + reply.getContent());
		} catch (CodecException | OntologyException e) {
			logError("Ontology/codec error occurred: " + e.getMessage());
			e.printStackTrace();
		} catch (FIPAException e) {
			logError("FIPA error occurred: " + e.getMessage());
			e.printStackTrace();
		}

		log("WeatherSplitterTester ending");
		doDelete();
	}

	private ACLMessage makeActionRequest(AID target, AgentAction action) throws CodecException, OntologyException {
		Ontology ontology = TaskOntology.getInstance();
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(target);
		msg.setLanguage(getCodec().getName());
		msg.setOntology(ontology.getName());
		getContentManager().fillContent(msg, new Action(target, action));
		return msg;
	}
}
