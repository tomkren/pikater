package org.pikater.shared.utilities.pikaterDatabase.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pikater.core.ontology.subtrees.batchDescription.model.ModelDescription;
import org.pikater.core.ontology.subtrees.batchDescription.model.NewModel;
import org.pikater.core.ontology.subtrees.experiment.Experiment;
import org.pikater.core.ontology.subtrees.experiment.experimentStatuses.ExperimentStatuses;
import org.pikater.core.ontology.subtrees.model.Model;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.exceptions.UserNotFoundException;
import org.pikater.shared.database.jpa.JPAAgentInfo;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.AbstractDAO.EmptyResultAction;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.security.PikaterPriviledge;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
import org.pikater.shared.database.utils.ResultFormatter;
import org.pikater.shared.database.views.base.SortOrder;
import org.pikater.shared.database.views.tableview.batches.AbstractBatchTableDBView;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBView;
import org.pikater.shared.database.views.tableview.externalagents.ExternalAgentTableDBView;
import org.pikater.shared.database.views.tableview.users.UsersTableDBView;

public class DatabaseTest {
	
	public void test(){
		//listDataSets();
		addExperiment();
		//listExternalAgents();
		//listDataSetWithExclusion();
		//listResults();
		//listUserAndRoles();
		//listBatches();
		//listExperiments();
		//listFileMappings();
		//listAgentInfos();
	}
	
	public void addExperiment(){
		JPAUser user=DAOs.userDAO.getByID(5856, EmptyResultAction.NULL);
		JPABatch jpaBatch=new JPABatch("test_batch_1","For test purposes","<?xml version=\"1.0\"?><batch><nope/></batch>",user);

		DAOs.batchDAO.storeEntity(jpaBatch);
		
		Experiment exp=new Experiment();
		exp.setBatchID(jpaBatch.getId());//62901);
		exp.setStatus(ExperimentStatuses.WAITING.toString());
		exp.setModel(new NewModel());
		
		int id1=DAOs.batchDAO.addExperimentToBatch(exp);
		System.out.println("Saved experiment with ID: "+id1);
		
		
		int modelID=12345678;
		Experiment exp2=new Experiment();
		exp2.setBatchID(jpaBatch.getId());//62901);
		exp2.setStatus(ExperimentStatuses.COMPUTING.toString());
		ModelDescription md=new ModelDescription();
		md.setModelID(modelID);
		exp2.setModel(md);
		
		int id2=DAOs.batchDAO.addExperimentToBatch(exp2);
		System.out.println("Saved experiment with ID: "+id2);
		
		int experimentID=id1;//63152;
		JPAResult jparesult=new JPAResult();
		jparesult.setAgentName("DBtest");
		jparesult.setNote("Example result from DB test");
		jparesult.setStart(new Date());
		jparesult.setFinish(new Date());
		
		int resultID=DAOs.experimentDAO.addResultToExperiment(experimentID, jparesult);
		System.out.println("Persisted JPAResult for experiment ID "+experimentID+" with ID: "+resultID);
		
		Model model=new Model(resultID, "java.lang.Object", "<?xml version=\"1.0\"?><model><nope/></model>");
		modelID=DAOs.resultDAO.setModelForResult(model);
		System.out.println("Saved model with ID: "+modelID);
	}
	
	public void listDataSets(){
		List<JPADataSetLO> dslos= DAOs.dataSetDAO.getAll(0,5,DataSetTableDBView.Column.APPROVE,SortOrder.DESCENDING);
		p("No. of found DataSets: "+dslos.size());
		for(JPADataSetLO dslo:dslos){
			p(dslo.getId()+". "+dslo.getHash()+"    "+dslo.getCreated()+"   DT:"+dslo.getGlobalMetaData().getNumberofInstances());
		}
		p("------------");
		p("");
	}
	
	public void listExternalAgents(){
		List<JPAExternalAgent> agents= DAOs.externalAgentDAO.getAll(0,5,ExternalAgentTableDBView.Column.AGENT_CLASS,SortOrder.DESCENDING);
		p("No. of found DataSets: "+agents.size());
		for(JPAExternalAgent ag:agents){
			p(ag.getId()+". "+ag.getAgentClass()+"    "+ag.getOwner().getLogin()+"  "+ag.getCreated()+"   "+ag.getDescription()+" ");
		}
		p("------------");
		p("");
	}
	
	public void listDataSetWithExclusion(){
		List<String> exList=new ArrayList<String>();
		List<JPADataSetLO> wDslos=DAOs.dataSetDAO.getByDescription("weather.arff");
		//Example for ResultFormatter
		ResultFormatter<JPADataSetLO> dsloFormatter=new ResultFormatter<JPADataSetLO>(wDslos);
		JPADataSetLO wdslo;
		try {
			wdslo = dsloFormatter.getSingleResult();
			exList.add(wdslo.getHash());
		} catch (NoResultException e) {
			System.err.println("weather.arff doesn't exist in the database: "+e.getMessage());
		}
		
		
		List<JPADataSetLO> iDslos=DAOs.dataSetDAO.getByDescription("iris.arff");
		if(iDslos.size()>0){
			JPADataSetLO idslo=iDslos.get(0);
			exList.add(idslo.getHash());
		}
		List<JPADataSetLO> dslos= DAOs.dataSetDAO.getAllExcludingHashes(exList);
		
		p("No. of found DataSets: "+dslos.size());
		System.out.print("Excluded: ");
		for(String s :exList){
			System.out.print(s+" ");
		}
		System.out.println();
		for(JPADataSetLO dslo:dslos){
			p(dslo.getId()+". "+dslo.getHash()+"    "+dslo.getCreated());
		}
		p("------------");
		p("");
	}
	
	public void listResults(){
		List<JPAResult> results=DAOs.resultDAO.getAll();
		for(JPAResult res:results){
			p(res.getId()+". "+res.getAgentName()+"    "+res.getStart());
		}
		p("------------");
		p("");
	}
	
	public void listUserAndRoles(){
		List<JPARole> roles=DAOs.roleDAO.getAll();
		p("No. of Roles in the system : "+roles.size());
		for(JPARole r:roles){
			p(r.getId()+". "+r.getName()+" : "+r.getRole().getDescription());
		}
		p("---------------------");
		p("");
		
		List<JPAUser> users=DAOs.userDAO.getAll(0, 5, UsersTableDBView.Column.RESET_PSWD, SortOrder.ASCENDING);
		p("No. of Users in the system : "+users.size());
		for(JPAUser r:users){
			p(r.getId()+". "+r.getLogin()+" : "+r.getStatus()+" - "+r.getEmail()+"   "+r.getCreated().toString()+" : SB = "+r.hasPrivilege(PikaterPriviledge.SAVE_BOX)+" : SDS = "+r.hasPrivilege(PikaterPriviledge.SAVE_DATA_SET));
		}
		p("---------------------");
		p("");
	}
	
	public void listBatches(){
		List<JPABatch> batches;//=DAOs.batchDAO.getAll();
		
		//batches=DAOs.batchDAO.getAll(0, 10,AbstractBatchTableDBView.Column.NAME);
		JPAUser user=new ResultFormatter<JPAUser>(DAOs.userDAO.getByLogin("klara")).getSingleResult();
		//batches=DAOs.batchDAO.getByOwner(user, 0, 10,AbstractBatchTableDBView.Column.CREATED,SortOrder.ASCENDING);
		batches=DAOs.batchDAO.getByOwnerAndStatus(user, JPABatchStatus.CREATED, 0, 10,AbstractBatchTableDBView.Column.CREATED,SortOrder.ASCENDING);
		p("No. of Batches in the system : "+DAOs.batchDAO.getByOwnerAndStatusCount(user, JPABatchStatus.CREATED));
		for(JPABatch b:batches){
			p(b.getOwner().getLogin()+":"+ b.getId()+". "+b.getName()+" : "+b.getCreated()+" - "+b.getFinished());
		}
		p("---------------------");
		p("");
	}
	
	public void listExperiments(){
		List<JPAExperiment> exps=DAOs.experimentDAO.getAll();
		p("No. of Experiments "+exps.size());
		for(JPAExperiment exp:exps){
			p(exp.getId()+". "+exp.getStatus()+" : "+exp.getStarted()+" - "+exp.getFinished());
		}
		
		p("---------------------");
		p("");
	}
	
	public void listFileMappings(){
		List<JPAFilemapping> fms=DAOs.filemappingDAO.getAll();
		p("No. of FileMappings "+fms.size());
		for(JPAFilemapping fm:fms){
			p(fm.getId()+". "+fm.getInternalfilename()+" - "+fm.getExternalfilename());
		}
		
		p("---------------------");
		p("");
	}
	
	public void listAgentInfos(){		
		List<JPAAgentInfo> ais=DAOs.agentInfoDAO.getAll();
		p("No. of AgentInfos "+ais.size());
		for(JPAAgentInfo ai:ais){
			p(ai.getId()+". "+ai.getAgentClass()+" '"+ai.getCreationTime()+"' - "+ai.getName()+" : "+ai.getDescription());
		}
		
		p("---------------------");
		p("");
	}
	
	
	private void p(String s){
		System.out.println(s);
	}
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException, UserNotFoundException{
		DatabaseTest dt=new DatabaseTest();
		dt.test();
		System.out.println("End of Database Testing");
	}
	
	
}
