package org.pikater.web.pikater;

import javax.servlet.ServletException;

import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

@SuppressWarnings("serial")
public class PikaterActionInitiator extends AchieveREInitiator {
    ACLMessage response;

    public PikaterActionInitiator(ACLMessage msg) {
        super(null, msg);
    }
    
    public ACLMessage getResponse() {
        return response;
    }
    
    /** Vrati zpravu s odpovedi pokud je to OK odpoved, jinak hodi ServletException. */
    public ACLMessage getOkResponse() throws ServletException {
        if (response == null)
            throw new ServletException("No response for Pikater agent action");
        if (ACLMessage.FAILURE == response.getPerformative() || ACLMessage.REFUSE == response.getPerformative())
            throw new ServletException("Pikater agent action failed or refused: "+response.getPerformative()+" " +response.getContent());
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
