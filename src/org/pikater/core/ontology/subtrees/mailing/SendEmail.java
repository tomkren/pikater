package org.pikater.core.ontology.subtrees.mailing;

import org.pikater.core.agents.system.Agent_Mailing.EmailType;

import jade.content.AgentAction;

/**
 * 
 * Represents a request for e-mail of a certain type
 *
 */
public class SendEmail implements AgentAction {
	
	private static final long serialVersionUID = 1437282103218542597L;

	private String emailType;
	private String toAddress;
	private Integer batchID;
	private Double result;

	public SendEmail() {
		// empty constructor for JADE
	}

	public SendEmail(EmailType emailType, String to) {
		this.emailType = emailType.name();
		this.toAddress = to;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public Integer getBatchId() {
		return batchID;
	}

	public void setBatchId(Integer batchID) {
		this.batchID = batchID;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}
}