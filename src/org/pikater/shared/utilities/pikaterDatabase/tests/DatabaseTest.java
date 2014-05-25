package org.pikater.shared.utilities.pikaterDatabase.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.exceptions.UserNotFoundException;
import org.pikater.shared.database.jpa.JPAAgentInfo;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.utils.ResultFormatter;

public class DatabaseTest {
	
	public void test(){
		listDataSets();
		listDataSetWithExclusion();
		listResults();
		listUserAndRoles();
		listBatches();
		listExperiments();
		listFileMappings();
		listAgentInfos();
	}
	
	public void listDataSets(){
		List<JPADataSetLO> dslos= DAOs.dataSetDAO.getAll();
		p("No. of found DataSets: "+dslos.size());
		for(JPADataSetLO dslo:dslos){
			p(dslo.getId()+". "+dslo.getHash()+"    "+dslo.getCreated());
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
			p(r.getId()+". "+r.getName()+" : "+r.getDescription());
		}
		p("---------------------");
		p("");
		
		List<JPAUser> users=DAOs.userDAO.getAll();
		p("No. of Users in the system : "+users.size());
		for(JPAUser r:users){
			p(r.getId()+". "+r.getLogin()+" : "+r.getStatus()+" - "+r.getEmail()+"   "+r.getCreated().toString());
		}
		p("---------------------");
		p("");
	}
	
	public void listBatches(){
		List<JPABatch> batches=DAOs.batchDAO.getAll();
		p("No. of Batches in the system : "+batches.size());
		for(JPABatch b:batches){
			p(b.getId()+". "+b.getName()+" : "+b.getCreated()+" - "+b.getFinished());
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
