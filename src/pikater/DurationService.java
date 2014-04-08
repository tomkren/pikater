package pikater;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import org.pikater.core.ontology.messages.DeleteTempFiles;
import org.pikater.core.ontology.messages.Duration;
import org.pikater.core.ontology.messages.ExecuteParameters;
import org.pikater.core.ontology.messages.GetAllMetadata;
import org.pikater.core.ontology.messages.GetDuration;
import org.pikater.core.ontology.messages.GetFileInfo;
import org.pikater.core.ontology.messages.GetFiles;
import org.pikater.core.ontology.messages.GetTheBestAgent;
import org.pikater.core.ontology.messages.ImportFile;
import org.pikater.core.ontology.messages.MessagesOntology;
import org.pikater.core.ontology.messages.Metadata;
import org.pikater.core.ontology.messages.SaveMetadata;
import org.pikater.core.ontology.messages.SaveResults;
import org.pikater.core.ontology.messages.Task;
import org.pikater.core.ontology.messages.TranslateFilename;
import org.pikater.core.ontology.messages.UpdateMetadata;

public class DurationService extends FIPAService {

	static final Codec codec = new SLCodec();

	public static Duration getDuration(Agent agent, GetDuration gd) {            

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID("duration", false));
		request.setOntology(MessagesOntology.getInstance().getName());
		request.setLanguage(codec.getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(gd);

		try {
			agent.getContentManager().fillContent(request, a);
		} catch (CodecException e1) {
			e1.printStackTrace();
		} catch (OntologyException e1) {
			e1.printStackTrace();
		}

		Duration duration = gd.getDuration();
		duration.setLR_duration(-1);
		try {						
			ACLMessage reply = FIPAService.doFipaRequestClient(agent, request);
			
			// get Duration from the received message			
			ContentElement content = agent.getContentManager().extractContent(reply);
							
			duration = (Duration)(((Result) content).getValue());									
			
		} catch (FIPAException e) {
			e.printStackTrace();
		} catch (UngroundedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return duration;
        }
}