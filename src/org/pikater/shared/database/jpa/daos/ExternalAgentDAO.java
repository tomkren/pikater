package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.utils.CustomActionResultFormatter;

public class ExternalAgentDAO extends AbstractDAO {
	
	public JPAExternalAgent getByClass(String cls) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			JPAExternalAgent res = null;
			List<JPAExternalAgent> results = em
				.createNamedQuery("ExternalAgent.getByClass",JPAExternalAgent.class)
				.setParameter("class", cls)
				.getResultList();
			if (!results.isEmpty()) {
				res = results.get(0);
			}
			return res;
		}finally{
			em.close();
		}
	}

	@Override
	public String getEntityName() {
		return JPAExternalAgent.class.getSimpleName();
	}

	@Override
	public List<JPAExternalAgent> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("ExternalAgent.getAll", JPAExternalAgent.class)
				.getResultList();
	}

	@Override
	public JPAExternalAgent getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPAExternalAgent>(
				getByTypedNamedQuery("ExternalAgent.getByID", "id", ID),
				era)
				.getSingleResultWithNull();
	}
	
	public List<JPAExternalAgent> getByOwner(JPAUser user) {
		return getByTypedNamedQuery("ExternalAgent.getByOwner", "owner", user);
	}
	
	private List<JPAExternalAgent> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAExternalAgent.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
}
