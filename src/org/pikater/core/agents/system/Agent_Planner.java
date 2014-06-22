package org.pikater.core.agents.system;


import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Vector;

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
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.ContractNetInitiator;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.configuration.Arguments;
import org.pikater.core.agents.system.managerAgent.ManagerAgentCommunicator;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

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
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		return ontologies;
	}

    private AID[] getAllAgents(String agentType){

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(agentType);
        template.addServices(serviceDescription);
        
        AID[] foundAgents = null;
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            foundAgents = new AID[result.length];
            for (int i = 0; i < result.length; ++i) {
                foundAgents[i] = result[i].getName();
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        return foundAgents;
    }


    protected void setup() {
            initDefault();

            registerWithDF(AgentNames.PLANNER);

            addBehaviour(new RequestServer(this));
    }


	protected class RequestServer extends CyclicBehaviour {
		private static final long serialVersionUID = -8439191651609121039L;

		Ontology ontology = TaskOntology.getInstance();

		private MessageTemplate reqMsgTemplate = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
				MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
								MessageTemplate.MatchOntology(ontology.getName()))));

		public RequestServer(PikaterAgent agent) {
			super(agent);
		}

		@Override
		public void action() {
			try {
				ACLMessage request = receive(reqMsgTemplate);
				if (request != null) {
					Action a = (Action) getContentManager().extractContent(request);
					
					if (a.getAction() instanceof ExecuteTask) {
						respondToExecuteTask(request, a);
					}

					ACLMessage result_msg = request.createReply();
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
		
		private void respondToExecuteTask(ACLMessage request, Action a) {
			
			ExecuteTask executeTask = (ExecuteTask) a.getAction();
			Task task = executeTask.getTask();
			String CAtype = task.getAgent().getType();

			AID[] foundAgents = getAllAgents(AgentNames.MANAGER_AGENT);
			// TODO choose a slave node
			ManagerAgentCommunicator comm = new ManagerAgentCommunicator(AgentNames.MANAGER_AGENT);

			log("about to create CA");
			AID ca = comm.createAgent(Agent_Planner.this, CAtype, CAtype, new Arguments());
			log("CA created");

			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(ca);
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.setLanguage(getCodec().getName());
			msg.setOntology(TaskOntology.getInstance().getName());
			
			Action a_ = new Action(myAgent.getAID(), executeTask);
			try {
				getContentManager().fillContent(msg, a_);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			addBehaviour(new doExecute(Agent_Planner.this, msg));
			return;
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

                    ExecuteTask execute = (ExecuteTask) (((Action) content).getAction());

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
