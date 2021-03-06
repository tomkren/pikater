package org.pikater.shared.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataTransferService;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.subtrees.file.PrepareFileUpload;
import org.pikater.shared.logging.database.PikaterDBLogger;

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
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(DataOntology.getInstance());
		return ontologies;
	}
	
	@Override
	protected void setup() {
		initDefault();
		getContentManager().registerOntology(DataOntology.getInstance());

		// dal by se taky najit v DF, kdybych nevedel, jak se jmenuje
		AID receiver = new AID(CoreAgents.DATA_MANAGER.getName(), false);

		PrepareFileUpload action = new PrepareFileUpload();
		action.setHash("772c551b8486b932aed784a582b9c1b1");

		try {
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			request.addReceiver(receiver);
			request.setLanguage(getCodec().getName());
			request.setOntology(DataOntology.getInstance().getName());
			getContentManager().fillContent(request, new Action(receiver, action));
			logInfo("Sending test request to dataManager");

			ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 10000);
			if (reply == null) {
				logSevere("Reply not received.");
			} else {
				logInfo("Reply received: " + ACLMessage.getPerformative(reply.getPerformative()) + " " + reply.getContent());
				int port = Integer.parseInt(reply.getContent());
				// tuhle cast uz muze delat jiny agent nez poslal PrepareFileUpload (ten by mel posilat master DataManager)
				DataTransferService.doClientFileTransfer("772c551b8486b932aed784a582b9c1b1_", "localhost", port);
			}
		} catch (CodecException | OntologyException e) {
			logException("Ontology/codec error occurred: " + e.getMessage(), e);
			e.printStackTrace();
		} catch (FIPAException e) {
			logException("FIPA error occurred: " + e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			logException("IO error occurred: " + e.getMessage(), e);
			PikaterDBLogger.logThrowable("Unexpected error occured:", e);
		}

		logInfo("FileUploadTester ending");
		doDelete();
	}

}
