package org.pikater.shared.database.jpa.daos;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAModel;
import org.pikater.shared.database.jpa.JPAResult;

public class ModelDAO extends AbstractDAO<JPAModel> {

	public ModelDAO(){
		super(JPAModel.class);
	}
	
	@Override
	public String getEntityName() {
		return JPAModel.EntityName;
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
	
}
