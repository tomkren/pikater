package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAAbstractEntity;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAModel;
import org.pikater.shared.database.utils.CustomActionResultFormatter;

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
