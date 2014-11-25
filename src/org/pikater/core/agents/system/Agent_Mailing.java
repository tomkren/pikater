package org.pikater.core.agents.system;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.MailingOntology;
import org.pikater.core.ontology.subtrees.mailing.SendEmail;
import org.pikater.shared.util.Mailing;

/**
 * 
 * Agent, which solves the communication with the local SMTP server, ie.
 * Sending various e-mail and their formation.
 *
 */
public class Agent_Mailing extends PikaterAgent {
	
	private static final long serialVersionUID = -2734321127446793005L;
	
	/**
	 * 
	 * Supported e-mail types
	 *
	 */
	public static enum EmailType {
		TEST, RESULT
	}

	/**
	 * Get ontologies which is using this agent
	 */
	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(MailingOntology.getInstance());

		return ontologies;
	}

	/**
	 * Agent setup
	 */
	@Override
	protected void setup() {
		initDefault();
		registerWithDF(CoreAgents.MAILING.getName());

		MessageTemplate template =
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
				
		addBehaviour(new AchieveREResponder(this, template) {
			private static final long serialVersionUID = 746138569142900592L;

			@Override
			protected ACLMessage handleRequest(final ACLMessage request
					) throws NotUnderstoodException, RefuseException {
				
				try {
					ContentElement element =
							getContentManager().extractContent(request);
					Concept action = ((Action) element).getAction();
					
					if (action instanceof SendEmail) {
						return respondToSendEmailAction(request);
					} else {
						throw new RefuseException("Invalid action requested");
					}
				} catch (OntologyException e) {
					Agent_Mailing.this.logException(e.getMessage(), e);
					throw new NotUnderstoodException(
							"Unknown ontology: " + e.getMessage());
					
				} catch (CodecException e) {
					Agent_Mailing.this.logException(e.getMessage(), e);
					throw new NotUnderstoodException(
							"Unknown codec: " + e.getMessage());
				}

			}
		});
	}

	/**
	 * Handle a request {@link SendEmail} and send back a answer
	 * @throws NotUnderstoodException
	 * @throws CodecException
	 * @throws OntologyException
	 */
	private ACLMessage respondToSendEmailAction(ACLMessage request
			) throws NotUnderstoodException, CodecException,
			OntologyException {
		
		ACLMessage reply = request.createReply();
		
		ContentElement element = getContentManager().extractContent(request);
		Action action = (Action) element;
		
		SendEmail sendEmail = (SendEmail) action.getAction();
		
		String to = sendEmail.getToAddress();

		EmailType type = getEmailType(sendEmail);
		try {
			switch (type) {
			case TEST:
				Mailing.sendEmail(to, "Test message",
						"Example message from pikater MailAgent");
				
				logInfo("sent e-mail to " + to);
				break;
			case RESULT:
				String body = "Dear user, your batch with ID " +
					sendEmail.getBatchId() + " is now finished.";
				
				if (sendEmail.getResult() != null) {
					body += "\n\nThe best error rate achieved was " +
						String.format("%.2f %%", sendEmail.getResult() * 100);
				}
				String subj = "Pikater batch finished (" +
					sendEmail.getBatchId() + ")";
				Mailing.sendEmail(to, subj, body);
				logInfo("sent e-mail to " + to);
				break;
			default:
				throw new UnsupportedOperationException();
			}
		} catch (MessagingException e) {
			String error = "Failed to dispatch e-mail for " +
				to + " : " + e.getMessage();
			logException(error, e);

			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent(error);
			return reply;
		}

		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		return reply;
	}

	/**
	 * A help function for conversion Enum values
	 * corresponding to the desired type of mail
	 * 
	 * @throws NotUnderstoodException
	 */
	private EmailType getEmailType(SendEmail mailAction
			) throws NotUnderstoodException {
		try {
			return EmailType.valueOf(mailAction.getEmailType());
		} catch (IllegalArgumentException e) {
			throw new NotUnderstoodException(
					"Unknown e-mail type " + mailAction.getEmailType());
		}
	}
}
