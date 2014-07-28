package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.core.ontology.subtrees.model.Model;
import org.pikater.shared.database.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAModel;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.utils.CustomActionResultFormatter;
import org.pikater.shared.database.utils.ResultFormatter;

public class ResultDAO extends AbstractDAO {

	@Override
	public String getEntityName() {
		return JPAResult.EntityName;
	}

	@Override
	public List<JPAResult> getAll() {
		return EntityManagerInstancesCreator
				.getEntityManagerInstance()
				.createNamedQuery("Result.getAll", JPAResult.class)
				.getResultList();
	}

	@Override
	public JPAResult getByID(int ID, EmptyResultAction era) {
		return new CustomActionResultFormatter<JPAResult>(
				getByTypedNamedQuery("Result.getByID", "id", ID),
				era
				).getSingleResultWithNull();
	}
	
	public List<JPAResult> getByAgentName(String agentName) {
		return getByTypedNamedQuery("Result.getByAgentName", "agentName", agentName);
	}
	
	public List<JPAResult> getByExperiment(JPAExperiment experiment) {
		return getByTypedNamedQuery("Result.getByExperiment", "experiment", experiment);
	}
	
	public List<JPAResult> getByDataSetHash(String dataSetHash) {
		return getByTypedNamedQuery("Result.getByDataSetHash", "hash", dataSetHash);
	}
	
	/**
	 * Persists a model for the specific result
	 * <p>
	 * The ID of the result is held by the {@link Model} object 
	 * @param model
	 * @return ID of persisted model, -1 if error happened
	 */
	public int setModelForResult(Model model){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		em.getTransaction().begin();
		try{
			JPAResult result=em.find(JPAResult.class, model.getResultID());
			if(result!=null){
				JPAModel jpaModel=new JPAModel();
				jpaModel.setAgentClassName(model.getAgentClassName());
				jpaModel.setSerializedAgent(model.getSerializedAgent());
				
				jpaModel.setCreatorResult(result);
				em.persist(jpaModel);
				result.setCreatedModel(jpaModel);
				em.getTransaction().commit();
				
				return jpaModel.getId();
			}else{
				em.getTransaction().rollback();
				return -1;
			}
		}finally{
			em.close();
		}
	}
	
	private List<JPAResult> getByTypedNamedQuery(String queryName,String paramName,Object param){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return
				em
				.createNamedQuery(queryName,JPAResult.class)
				.setParameter(paramName, param)
				.getResultList();
		}finally{
			em.close();
		}
	}	
}
