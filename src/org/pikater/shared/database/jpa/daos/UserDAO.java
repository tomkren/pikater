package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.status.JPAUserStatus;
import org.pikater.shared.database.utils.ResultFormatter;

public class UserDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return JPAUser.EntityName;
	}

	@Override
	public List<JPAUser> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("User.getAll", JPAUser.class)
				.getResultList();
	}

	@Override
	public JPAUser getByID(int ID) {
		return new ResultFormatter<JPAUser>(
				getByTypedNamedQuery("User.getByID", "id", ID)
				).getSingleResultWithNull();
	}
	
	@Override
	public JPAUser getByIDWithException(int ID)
			throws NoResultException {
		return new ResultFormatter<JPAUser>(
				getByTypedNamedQuery("User.getByID", "id", ID)
				).getSingleResult();
	}
	
	public List<JPAUser> getByStatus(JPAUserStatus status) {
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

	public void updateEntity(JPAUser changedEntity){
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPAUser item=em.find(JPAUser.class, changedEntity.getId());
			item.updateValues(changedEntity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't update JPA User object.", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
}
