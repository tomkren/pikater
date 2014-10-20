package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPATaskType;

public class TaskTypeDAO extends AbstractDAO<JPATaskType> {

	public TaskTypeDAO() {
		super(JPATaskType.class);
	}

	@Override
	public String getEntityName() {
		return JPATaskType.ENTITYNAME;
	}

	public JPATaskType createOrGetByName(String name) {
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		try {
			em.getTransaction().begin();
			List<JPATaskType> tasks = em.createNamedQuery("TaskType.getByName", JPATaskType.class).setParameter("name", name).getResultList();
			if (!tasks.isEmpty()) {
				em.getTransaction().commit();
				return tasks.get(0);
			} else {
				JPATaskType newTT = new JPATaskType();
				newTT.setName(name);
				DAOs.TASKTYPEDAO.storeEntity(newTT);
				return newTT;
			}
		} catch (Exception e) {
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		return null;
	}
}
