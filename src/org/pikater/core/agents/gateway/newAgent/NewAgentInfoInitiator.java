package org.pikater.core.agents.gateway.newAgent;

import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class NewAgentInfoInitiator extends AchieveREInitiator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -364766692797277164L;

	private ACLMessage response;

	public NewAgentInfoInitiator(ACLMessage msg) {
		super(null, msg);
	}

	public ACLMessage getResponse() {
		return response;
	}

    /** Returns the response message if response is OK, or returns a Exception. 
     * @throws Exception
     */
    public ACLMessage getOkResponse() throws Exception {

        if (response == null) {
            throw new Exception("No response for Pikater agent action");
        }

        if (ACLMessage.FAILURE == response.getPerformative() ||
        		ACLMessage.REFUSE == response.getPerformative()) {
            throw new Exception("Pikater agent action failed or refused: "+response.getPerformative()+" " +response.getContent());
        }
        return response;
    }
    
	@Override
	protected void handleInform(ACLMessage inform) {
		response = inform;
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
		response = refuse;
	}

	@Override
	protected void handleFailure(ACLMessage failure) {
		response = failure;
	}
}
