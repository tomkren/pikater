package org.pikater.core.agents.system;

import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.MailingOntology;
import org.pikater.core.ontology.subtrees.mailing.SendEmail;

/** Agent, ktery resi komunikaci s mistnim SMTP serverem, tj. odesilani ruznych e-mailu i jejich tvorbu. */
public class Agent_Mailing extends PikaterAgent {
    private static final long serialVersionUID = -2734321127446793005L;

    /** Podporovane typy e-mailu */
    public static enum EmailType { TEST, RESULT };

    /** Adresa, ktera bude uvedena ve FROM u mailu, ktere agent posle
     * TODO: nekam do konfigurace? */
    private static final String SENDER_EMAIL = "pikater@noreply.cz";

	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(MailingOntology.getInstance());
		
		return ontologies;
	}

    @Override
    protected void setup() {
        initDefault();
        registerWithDF(AgentNames.MAILING);
        
        addBehaviour(new AchieveREResponder(this, MessageTemplate.MatchPerformative(ACLMessage.REQUEST)) {
            private static final long serialVersionUID = 746138569142900592L;

            @Override
            protected ACLMessage handleRequest(final ACLMessage request) throws NotUnderstoodException, RefuseException {
                try {
                    Concept action = ((Action)(getContentManager().extractContent(request))).getAction();
                    if (action instanceof SendEmail)
                        return respondToSendEmailAction(request);
                    else
                        throw new RefuseException("Invalid action requested");
                } catch (OntologyException e) {
                	Agent_Mailing.this.logError(e.getMessage(), e);
                    throw new NotUnderstoodException("Unknown ontology: "+e.getMessage());
                } catch (CodecException e) {
                	Agent_Mailing.this.logError(e.getMessage(), e);
                    throw new NotUnderstoodException("Unknown codec: "+e.getMessage());
                }
          
            }
        });
    }

    /** Provede pozadavek typu SendEmail a vrati odpoved */
    private ACLMessage respondToSendEmailAction(ACLMessage request) throws NotUnderstoodException, CodecException, OntologyException {
        ACLMessage reply = request.createReply();
        SendEmail mailAction = (SendEmail)((Action)(getContentManager().extractContent(request))).getAction();
        String to = mailAction.getTo_address();

        EmailType type = getEmailType(mailAction);
        try {
            switch (type) {
            case TEST:
                sendEmail(to, "Test message", "Example message from pikater MailAgent");
                break;
            case RESULT:
                sendEmail("jkr.sw@atlas.cz", "results message", "TODO"); //XXX adresa
                break;
            default:
                throw new UnsupportedOperationException();
            }
        } catch (MessagingException e) {
            String error = "Failed to dispatch e-mail for "+to+" : "+e.getMessage();
            logError(error, e);

            reply.setPerformative(ACLMessage.FAILURE);
            reply.setContent(error);
            return reply;
        }

        reply.setPerformative(ACLMessage.INFORM);
        reply.setContent("OK");
        return reply;
    }

    /** Pomocna funkce pro vytazeni Enum hodnoty odpovidajici typu pozadovaneho mailu */
    private EmailType getEmailType(SendEmail mailAction) throws NotUnderstoodException {
        try {
            return EmailType.valueOf(mailAction.getEmail_type());
        } catch (IllegalArgumentException e) {
            throw new NotUnderstoodException("Unknown e-mail type "+mailAction.getEmail_type());
        }
    }

    /** Odesle plaintext e-mail dle parametru pomoci lokalniho SMTP serveru, nebo vyhodi vyjimku. */
    private void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties properties = new Properties();
        // predpokladam funkcni SMTP server na localhostu
        properties.setProperty("mail.smtp.host", "localhost");
        
        MimeMessage message = new MimeMessage(Session.getDefaultInstance(properties));
        // od koho
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        // komu
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // predmet
        message.setSubject(subject);
        // telo
        message.setText(body);

        Transport.send(message);
        
        log("sent e-mail to "+to);
    }

}
