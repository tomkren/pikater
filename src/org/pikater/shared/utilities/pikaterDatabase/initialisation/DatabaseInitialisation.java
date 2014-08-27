package org.pikater.shared.utilities.pikaterDatabase.initialisation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.pikater.core.agents.system.metadata.reader.JPAMetaDataReader;
import org.pikater.shared.database.exceptions.UserNotFoundException;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.JPAUserPriviledge;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.security.PikaterPriviledge;
import org.pikater.shared.database.jpa.security.PikaterRole;
import org.pikater.shared.database.jpa.status.JPADatasetSource;
import org.pikater.shared.database.jpa.status.JPAUserStatus;
import org.pikater.shared.database.util.CustomActionResultFormatter;
import org.pikater.shared.database.util.Hash;
import org.pikater.shared.util.DateUtils;

public class DatabaseInitialisation {
	
	private static final String dataSetPath="core/datasets";
	
	private void init() throws UserNotFoundException, FileNotFoundException, IOException, SQLException, ParseException{
		
		//DatabaseTest dbTest=new DatabaseTest();
		
		this.createRolesAndUsers();
		//dbTest.listUserAndRoles();
		
		/**
		this.addWebDatasets();
		dbTest.listDataSets();
		**/
		
		//addExternalAgent("core/ext_agents/org_pikater_external_ExternalWekaAgent.jar", "ExternalTestingAgent", "Testing agent from JAR");
		//listExternalAgents();
	}
	
	
	protected void addWebDatasets() throws FileNotFoundException, IOException, UserNotFoundException, SQLException{
		File dir=new File(DatabaseInitialisation.dataSetPath);
		
		JPAUser owner = DAOs.userDAO.getByLogin("stepan").get(0);
		System.out.println("Target user: "+owner.getLogin());
		
		File[] datasets=dir.listFiles();
		for(File datasetI : datasets){
			if(datasetI.isFile()){
				try{
				System.out.println("--------------------");
				System.out.println("Dataset: "+datasetI.getAbsolutePath());
				
				DAOs.dataSetDAO.storeNewDataSet(datasetI, datasetI.getName(), owner.getId(),JPADatasetSource.USER_UPLOAD);
				
				System.out.println("--------------------");
				System.out.println();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		
		
		
		///Update metadata
		/**
		
		File[] datasets2=dir.listFiles();
		for(File datasetI : datasets2){
			if(datasetI.isFile()){
				try{
				System.out.println("--------------------");
				System.out.println("Updating metadata for : "+datasetI.getAbsolutePath());
				this.updateMetaDataForHash(datasetI);
				
				System.out.println("--------------------");
				System.out.println();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		**/
		
		
	}
	
	protected void updateMetaDataForHash(File file) throws IOException{
		
		String hash = Hash.getMD5Hash(file);
		List<JPADataSetLO> dsloDataSetLO=DAOs.dataSetDAO.getByHash(hash);
		if(dsloDataSetLO.size()>0){
			JPADataSetLO dslo=dsloDataSetLO.get(0);
			
			JPAMetaDataReader readr=new JPAMetaDataReader();
			try {
				readr.readFile(file);
				
				dslo.setGlobalMetaData(readr.getJPAGlobalMetaData());
				dslo.setAttributeMetaData(readr.getJPAAttributeMetaData());
				
				DAOs.dataSetDAO.updateEntity(dslo);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else{
			System.out.println("DataSet not found");
		}
	}
	
	private void createRoles(){
		for(PikaterPriviledge priv : PikaterPriviledge.values()){
			DAOs.userPrivDAO.storeEntity(
					  new JPAUserPriviledge(priv.name(), priv)
					);
		}
		
		JPAUserPriviledge sdsPriv = DAOs.userPrivDAO.getByName(PikaterPriviledge.SAVE_DATA_SET.name());
		JPAUserPriviledge sbPriv = DAOs.userPrivDAO.getByName(PikaterPriviledge.SAVE_BOX.name());
		
		JPARole u= new JPARole(PikaterRole.USER.name(),PikaterRole.USER);
		u.addPriviledge(sdsPriv);
		DAOs.roleDAO.storeEntity(u);
		
		JPARole a= new JPARole(PikaterRole.ADMIN.name(),PikaterRole.ADMIN);
		a.addPriviledge(sdsPriv);
		a.addPriviledge(sbPriv);
		DAOs.roleDAO.storeEntity(a);
	}
	
	private void createUsers(){
		JPARole u = DAOs.roleDAO.getByPikaterRole(PikaterRole.USER);
		JPARole a = DAOs.roleDAO.getByPikaterRole(PikaterRole.ADMIN);
		
		JPAUser u0=JPAUser.createAccountForDBInit("zombie","xxx", "invalid@mail.com", u);
		u0.setPriorityMax(-1);
		u0.setStatus(JPAUserStatus.SUSPENDED);
		DAOs.userDAO.storeEntity(u0);
		
		
		JPAUser u1=JPAUser.createAccountForDBInit("stepan","123", "bc.stepan.balcar@gmail.com", a);
		DAOs.userDAO.storeEntity(u1);
		
		
		JPAUser u2=JPAUser.createAccountForDBInit("kj","123", "kj@gmail.com", a);
		DAOs.userDAO.storeEntity(u2);
	
		
		JPAUser u3=JPAUser.createAccountForDBInit("sj","123", "kukurka@gmail.com", a);
		DAOs.userDAO.storeEntity(u3);
		
		
		JPAUser u4=JPAUser.createAccountForDBInit("sp","123", "sp@gmail.com", a);
		DAOs.userDAO.storeEntity(u4);
		
		
		JPAUser u5=JPAUser.createAccountForDBInit("martin", "123", "Martin.Pilat@mff.cuni.cz", u);
		DAOs.userDAO.storeEntity(u5);
		
		
		JPAUser u6=JPAUser.createAccountForDBInit("klara", "123", "peskova@braille.mff.cuni.cz", u);
		DAOs.userDAO.storeEntity(u6);
	}
	
	private void createRolesAndUsers() {		
		createRoles();
		createUsers();
	}
	
	
	public void addExternalAgent(String jar, String name, String desc) {
		String cls = FilenameUtils.getBaseName(jar).replace(".jar", "").replace("_", ".");
		if (DAOs.externalAgentDAO.getByAgentClass(cls) != null) {
			p("External agent "+ name +" already in DB.");
			return;
		}
		JPAUser owner=new CustomActionResultFormatter<JPAUser>(
				DAOs.userDAO.getByLogin("sj"))
				.getSingleResultWithNull();
		
		JPAExternalAgent e = new JPAExternalAgent();
		e.setAgentClass(cls);
		e.setName(name);
		e.setDescription(desc);
		e.setOwner(owner);
		e.setCreated(new Date());
		byte[] content;
		try {
			content = Files.readAllBytes(Paths.get(jar));
		} catch (IOException e1) {
			p("Failed to read JAR "+jar);
			e1.printStackTrace();
			return;
		}
		e.setJar(content);
		DAOs.externalAgentDAO.storeEntity(e);
		p("Stored external agent "+ name);
	}
	
	public void listExternalAgents(){
		List<JPAExternalAgent> agents=DAOs.externalAgentDAO.getAll();
		p("");
		p("Available external agents: ");
		for(JPAExternalAgent agent:agents){
			p(agent.getName()+" "+agent.getAgentClass()+" \""+agent.getDescription()+"\" "+DateUtils.toCzechDate(agent.getCreated()));
		}
	}
	
	private void p(String s){
		System.out.println(s);
	}


	public static void main(String[] args) throws SQLException, IOException, UserNotFoundException, ClassNotFoundException, ParseException {
		DatabaseInitialisation data = new DatabaseInitialisation();
		data.init();
		System.out.println("End of Database Initialisation");
	}
}
