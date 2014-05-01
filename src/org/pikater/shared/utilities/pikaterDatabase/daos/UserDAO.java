package org.pikater.shared.utilities.pikaterDatabase.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.UserStatus;
import org.pikater.shared.utilities.pikaterDatabase.newDB.AbstractDAO;
import org.pikater.shared.utilities.pikaterDatabase.newDB.EntityManagerInstancesCreator;

public class UserDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return (new JPAUser()).getEntityName();
	}

	@Override
	public List<JPAUser> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("User.getAll", JPAUser.class)
				.getResultList();
	}

	@Override
	public List<JPAUser> getByID(int ID) {
		return getByTypedNamedQuery("User.getByID", "id", ID);
	}
	
	public List<JPAUser> getByStatus(UserStatus status) {
		return getByTypedNamedQuery("User.getByStatus", "status", status);
	}
	
	public List<JPAUser> getByLogin(String login) {
		return getByTypedNamedQuery("User.getByLogin", "login", login);
	}
	
	public List<JPAUser> getByRole(JPARole role) {
		return getByTypedNamedQuery("User.getByRole", "role", role);
	}
	
	private List<JPAUser> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAUser.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}

	
}
