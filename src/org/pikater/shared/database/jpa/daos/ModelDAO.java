package org.pikater.shared.database.jpa.daos;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAModel;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.util.CustomActionResultFormatter;

public class ModelDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return JPAModel.EntityName;
	}

	@Override
	public List<JPAModel> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Model.getAll", JPAModel.class)
				.getResultList();
	}

	@Override
	public JPAModel getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPAModel>(
				this.getByTypedNamedQuery("Model.getByID", "id", ID), era).getSingleResultWithNull();
	}
	
	public List<JPAModel> getByAgentClassName(String agentClassName) {
		return this.getByTypedNamedQuery("Model.getByAgentClassName", "agentClassName", agentClassName);
	}
	
	/**
	 * Removes models, that are older than current datetime - given days and
	 * doesn't have permanent flag.
	 * @param daysback maximum age of models, that should be left in the database
	 */
	public void removeOldModels(int daysback){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			em.getTransaction().begin();
			Date date=new Date(new Date().getTime() - daysback*24*60*60*1000  );
			
			List<Object[]> results=
				em
				.createNamedQuery("Model.getNotPermanentOlderThan",Object[].class)
				.setParameter("paramDate", date, TemporalType.TIMESTAMP)
				.getResultList();
			
			for(Object[] res:results){
				JPAModel model=(JPAModel)res[0];
				JPAResult result=(JPAResult)res[1];
				result.setCreatedModel(null);
				em.remove(model);
			}
			
			em.getTransaction().commit();
		}finally{
			em.close();
		}
	}
	
	private List<JPAModel> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAModel.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}
	
}
