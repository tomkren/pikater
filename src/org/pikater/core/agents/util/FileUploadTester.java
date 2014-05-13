package org.pikater.core.agents.util;

import java.io.IOException;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.Agent_Mailing;
import org.pikater.core.agents.system.data.DataTransferService;
import org.pikater.core.ontology.actions.DataOntology;
import org.pikater.core.ontology.actions.MailingOntology;
import org.pikater.core.ontology.data.PrepareFileUpload;
import org.pikater.core.ontology.mailing.SendEmail;

import jade.content.AgentAction;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;

/** Tento agent slouzi k vyzkouseni uploadu souboru od DataManagera. */
public class FileUploadTester extends PikaterAgent {
	private static final long serialVersionUID = -8946304470396671885L;

	@Override
	protected void setup() {
		initDefault();
		getContentManager().registerOntology(DataOntology.getInstance());

		// dal by se taky najit v DF, kdybych nevedel, jak se jmenuje
		AID receiver = new AID(AgentNames.DATA_MANAGER, false);

		PrepareFileUpload action = new PrepareFileUpload();
		action.setHash("772c551b8486b932aed784a582b9c1b1");

		try {
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			request.addReceiver(receiver);
			request.setLanguage(getCodec().getName());
			request.setOntology(DataOntology.getInstance().getName());
			getContentManager().fillContent(request, new Action(receiver, action));
			log("Sending test request to dataManager");

			ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 10000);
			if (reply == null)
				logError("Reply not received.");
			else
				log("Reply received: " + ACLMessage.getPerformative(reply.getPerformative()) + " " + reply.getContent());

			int port = Integer.parseInt(reply.getContent());
			// tuhle cast uz muze delat jiny agent nez poslal PrepareFileUpload (ten by mel posilat master DataManager)
			DataTransferService.doClientFileTransfer("772c551b8486b932aed784a582b9c1b1_", "localhost", port);
		} catch (CodecException | OntologyException e) {
			logError("Ontology/codec error occurred: " + e.getMessage());
			e.printStackTrace();
		} catch (FIPAException e) {
			logError("FIPA error occurred: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logError("IO error occurred: " + e.getMessage());
			e.printStackTrace();
		}

		log("FileUploadTester ending");
		doDelete();
	}
}
