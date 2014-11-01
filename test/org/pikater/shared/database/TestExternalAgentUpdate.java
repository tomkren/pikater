package org.pikater.shared.database;

import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.daos.DAOs;

public class TestExternalAgentUpdate {
	public static void main(String[] args) {
		JPAExternalAgent ea = DAOs.externalAgentDAO.getAll(0, 1).get(0);
		ea.setAgentClass("org.pikater.external.ExternalWekaAgent");
		ea.setDescription("Testing agent from JAR");
		ea.setName("ExternalTestingAgent");
		ea.setApproved(false);
		ea.setVisible(true);
		DAOs.externalAgentDAO.updateEntity(ea);
	}
}
