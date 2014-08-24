package org.pikater.shared.database.jpa.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.pikater.core.ontology.subtrees.model.Model;
import org.pikater.shared.database.jpa.EntityManagerInstancesCreator;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAModel;
import org.pikater.shared.database.jpa.JPAResult;

public class ResultDAO extends AbstractDAO<JPAResult> {
	
	public ResultDAO(){
		super(JPAResult.class);
	}

	@Override
	public String getEntityName() {
		return JPAResult.EntityName;
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
	
	public List<JPAResult> getResultsByDataSetHashAscendingUponErrorRate(String dataSetHash,int count){
	    return getByTypedNamedQuery(
	    		"Result.getByDataSetHashErrorAscending",
	    		"hash",
	    		dataSetHash,
	    		0,
	    		count);
	}
	
	/**
	 * Returns the Result for Experiment with the minimal ErrorRate
	 * @param experiment {@link JPAExperiment} for which we want get the best result
	 * @return {@link JPAResult} with minimal error rate or null if no results available
	 */
	public JPAResult getByExperimentBestResult(JPAExperiment experiment){
		EntityManager em=EntityManagerInstancesCreator.getEntityManagerInstance();
		try{
			return em
				.createNamedQuery("Result.getByExperimentErrorAscending", JPAResult.class)
				.setParameter("experiment", experiment)
				.setMaxResults(1)//fix exception, when more than one result is returned
				.getSingleResult();
		}catch(Exception e){
			return null;
		}finally{
			em.close();
		}
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
	
	public void deleteResultByEntity(JPAResult result){
		this.deleteResultByID(result.getId());
	}
	
	public void deleteResultByID(int id){
		this.deleteEntityByID(id);
	}
}
