package org.pikater.shared.utilities.pikaterDatabase.newDB;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.utilities.logging.PikaterLogger;



public abstract class AbstractDAO
{
	protected static Logger logger=PikaterLogger.getLogger(
		    Thread.currentThread().getStackTrace()[0].getClassName() );

	public abstract String getEntityName();	
	public abstract <R extends JPAAbstractEntity> List<R> getAll();
	public abstract <R> List<R> getByID(int ID);
	public boolean existsByID(int ID){
		return getByID(ID).size()>0;
	}
	
	public void updateEntity(JPAAbstractEntity changedEntity){
		EntityManager em = EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPAAbstractEntity item=em.find(JPAAbstractEntity.class, changedEntity.getId());
			item.updateValues(changedEntity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't update JPA DataSetLO object.", e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
	
	public void storeEntity(Object newEntity){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			em.persist(newEntity);
			em.getTransaction().commit();
		}catch(Exception e){
			logger.error("Can't persist JPA object.",e);
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}
	
	
	
}
