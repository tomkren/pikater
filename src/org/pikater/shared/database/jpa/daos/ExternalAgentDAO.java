package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.jpa.JPAExternalAgent;

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
	public <R extends JPAAbstractEntity> List<R> getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends JPAAbstractEntity> R getByID(int ID, EmptyResultAction era) {
		throw new UnsupportedOperationException();
	}
}
