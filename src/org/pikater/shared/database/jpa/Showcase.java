package org.pikater.shared.database.jpa;

import java.util.Date;

import org.pikater.shared.database.jpa.daos.DAOs;

public class Showcase {
	public static void createAndStoreEntity() {
		JPAUser user = new JPAUser(); // NOTE: in practice, we have to provide login, password etc.
		DAOs.userDAO.storeEntity(user);
	}

	public static void getAndUpdateEntity() {
		JPAUser user = DAOs.userDAO.getByID(0); // NOTE: in practice, we have to provide a custom ID
		user.setLastLogin(new Date());
		DAOs.userDAO.updateEntity(user);
	}

	public static void getAndDeleteEntity() {
		JPAUser user = DAOs.userDAO.getByID(0); // NOTE: in practice, we have to provide a custom ID
		DAOs.userDAO.deleteEntity(user);
	}
}