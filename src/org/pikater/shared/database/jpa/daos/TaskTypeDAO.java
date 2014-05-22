package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPATaskType;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.UserStatus;

public class TaskTypeDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return (new JPATaskType()).getEntityName();
	}

	@Override
	public List<JPATaskType> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("TaskType.getAll", JPATaskType.class)
				.getResultList();
	}

	@Override
	public List<JPATaskType> getByID(int ID) {
		return getByTypedNamedQuery("TaskType.getByID", "id", ID);
	}
	
	public JPATaskType createOrGetByName(String name) {
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			em.getTransaction().begin();
			List<JPATaskType> tasks=
					em.
					createNamedQuery("TaskType.getByName", JPATaskType.class)
					.setParameter("name", name)
					.getResultList();
			if(tasks.size()>0){
				em.getTransaction().commit();
				return tasks.get(0);
			}else{
				JPATaskType newTT=new JPATaskType();
				newTT.setName(name);
				DAOs.taskTypeDAO.storeEntity(newTT);
				return newTT;
			}
		}catch(Exception e){
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
		return null;
	}
	
	
	private List<JPATaskType> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPATaskType.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}

	
}
