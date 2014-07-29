package org.pikater.core.agents.gateway;

import org.pikater.core.agents.gateway.exception.PikaterGatewayException;

import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class Initiator extends AchieveREInitiator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -364766692797277164L;

	protected ACLMessage response;

	public Initiator(ACLMessage msg) {
		super(null, msg);
	}

	public ACLMessage getResponse() {
		return response;
	}

    /** Returns the response message if response is OK, or returns a Exception. 
     * @throws Exception
     */
    public ACLMessage getOkResponse() throws PikaterGatewayException {

        if (response == null) {
            throw new PikaterGatewayException("No response for Pikater agent action");
        }

        if (ACLMessage.FAILURE == response.getPerformative() ||
        		ACLMessage.REFUSE == response.getPerformative()) {
            throw new PikaterGatewayException("Pikater agent action failed or refused: "+response.getPerformative()+" " +response.getContent());
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
