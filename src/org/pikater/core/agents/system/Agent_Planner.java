package org.pikater.core.agents.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Random;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.ContractNetInitiator;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.configuration.Argument;
import org.pikater.core.agents.configuration.Arguments;
import org.pikater.core.agents.system.management.ManagerAgentCommunicator;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.MessagesOntology;
import org.pikater.core.ontology.subtrees.task.Execute;

/**
 * Created with IntelliJ IDEA.
 * User: Klara
 * Date: 3/16/14
 * Time: 1:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class Agent_Planner extends PikaterAgent {
	private static final long serialVersionUID = 820846175393846627L;

	protected LinkedList<ACLMessage> requestsFIFO = new LinkedList<ACLMessage>();
    protected int nResponders;

    @Override
	public java.util.List<Ontology> getOntologies() {
		java.util.List<Ontology> ontologies = new java.util.ArrayList<Ontology>();
		ontologies.add(MessagesOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		return ontologies;
	}

    @Deprecated
    private AID[] getAllComputingAgents(){
        AID[] computingAgents = null;

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(AgentNames.COMPUTING_AGENT);
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            computingAgents = new AID[result.length];
            for (int i = 0; i < result.length; ++i) {
                computingAgents[i] = result[i].getName();
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        return computingAgents;
    }


    protected void setup() {
            initDefault();

            registerWithDF(AgentNames.PLANNER);

            addBehaviour(new RequestServer(this));
    }


	protected class RequestServer extends CyclicBehaviour {
		private static final long serialVersionUID = -8439191651609121039L;

		Ontology ontology = MessagesOntology.getInstance();

		private MessageTemplate reqMsgTemplate = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
				MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()), MessageTemplate.MatchOntology(ontology.getName()))));

		public RequestServer(PikaterAgent agent) {
			super(agent);
		}

		@Override
		public void action() {
			try {
				ACLMessage req = receive(reqMsgTemplate);
				if (req != null) {
					ContentElement content = getContentManager().extractContent(req);
					if (((Action) content).getAction() instanceof Execute) {
						// create agent
						// TODO pass the right type somewhere
						String CAtype = "RBFNetwork";

						// TODO choose a slave node
						ManagerAgentCommunicator comm = new ManagerAgentCommunicator(AgentNames.MANAGER_AGENT);

						log("about to create CA");
						AID ca = comm.createAgent(Agent_Planner.this, CAtype, CAtype + Math.abs((new Random()).nextInt()), new Arguments());
						log("CA created");

						ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
						msg.addReceiver(ca);
						msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
						msg.setLanguage(getCodec().getName());
						msg.setOntology(MessagesOntology.getInstance().getName());
						Execute ex = (Execute) ((Action) content).getAction();
						Action a = new Action(myAgent.getAID(), ex);
						getContentManager().fillContent(msg, a);

						addBehaviour(new doExecute(Agent_Planner.this, msg));
						return;
					}

					ACLMessage result_msg = req.createReply();
					result_msg.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					send(result_msg);
					return;
				}
			} catch (Codec.CodecException ce) {
				ce.printStackTrace();
			} catch (OntologyException oe) {
				oe.printStackTrace();
			}
		}
	}
	
	/** Assigns a task to a newly created computing agent */
	protected class doExecute extends AchieveREInitiator {
		private static final long serialVersionUID = 1572211801881987607L;

		public doExecute(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			log("Agent "+inform.getSender().getName()+" successfully performed the requested action");
		}

		@Override
		protected void handleRefuse(ACLMessage refuse) {
			logError("Execute was refused");
		}

		@Override
		protected void handleFailure(ACLMessage failure) {
			logError("Agent "+failure.getSender().getName()+" failed to perform the requested action: "+failure.getContent());
		}
		
	}
	
	@Deprecated
	protected class askComputingAgents extends ContractNetInitiator {
		private static final long serialVersionUID = -7890655626575943947L;

		ACLMessage req;
        ACLMessage cfp;

        public askComputingAgents(Agent agent, ACLMessage _cfp, ACLMessage _req) {
            super(agent, _cfp);
            cfp = _cfp;
            req = _req;
        }

        protected void handlePropose(ACLMessage propose, Vector v) {
            log("Agent " + propose.getSender().getName() + " proposed " + propose.getContent());
        }

        protected void handleRefuse(ACLMessage refuse) {
            log("Agent " + refuse.getSender().getName() + " refused");
        }

        protected void handleFailure(ACLMessage failure) {
            if (failure.getSender().equals(myAgent.getAMS())) {
                // FAILURE notification from the JADE runtime: the receiver
                // does not exist
                log("Responder does not exist");
            }
            else {
                log("Agent " + failure.getSender().getName() + " failed");
            }
            // Immediate failure --> we will not receive a response from this agent
            nResponders--;
        }

        protected void handleAllResponses(Vector responses, Vector acceptances) {
            if (responses.size() < nResponders) {
                // Some responder didn't reply within the specified timeout
                log("Timeout expired: missing " + (nResponders - responses.size()) + " responses.");
            }
            // Evaluate proposals.
            int bestProposal = -1;
            AID bestProposer = null;
            ACLMessage accept = null;
            Enumeration e = responses.elements();
            while (e.hasMoreElements()) {
                ACLMessage msg = (ACLMessage) e.nextElement();
                if (msg.getPerformative() == ACLMessage.PROPOSE) {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    acceptances.addElement(reply);
                    int proposal = Integer.parseInt(msg.getContent());
                    if (proposal > bestProposal) {
                        bestProposal = proposal;
                        bestProposer = msg.getSender();
                        accept = reply;
                    }
                }
            }
            // Accept the proposal of the best proposer
            if (accept != null) {
                log("Accepting proposal " + bestProposal + " from responder " + bestProposer.getName());
                accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                try {
                    ContentElement content = getContentManager().extractContent(cfp);

                    Execute execute = (Execute) (((Action) content).getAction());

                    Action a = new Action();
                    a.setAction(execute);
                    a.setActor(myAgent.getAID());

                    getContentManager().fillContent(accept, a);
                } catch (UngroundedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (CodecException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (OntologyException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

        protected void handleInform(ACLMessage _inform) {
            log("Agent "+_inform.getSender().getName()+" successfully performed the requested action");

            ACLMessage inform = req.createReply();
            inform.setPerformative(ACLMessage.INFORM);

            try {
                ContentElement content = getContentManager().extractContent(_inform);
                getContentManager().fillContent(inform, content);
            } catch (CodecException e) {
                e.printStackTrace();
            } catch (OntologyException e) {
                e.printStackTrace();
            }

            send(inform);
        }
    }
}
