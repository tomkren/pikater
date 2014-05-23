package org.pikater.shared.utilities.pikaterDatabase.initialisation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.postgresql.PGConnection;
import org.pikater.core.agents.system.Agent_DataManager;
import org.pikater.shared.database.exceptions.UserNotFoundException;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.JPAUserPriviledge;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;
import org.pikater.shared.database.jpa.status.JPAUserStatus;
import org.pikater.shared.database.utils.Hash;
import org.pikater.shared.database.utils.JPAMetaDataReader;
import org.pikater.shared.database.PostgreSQLConnectionProvider;
import org.pikater.shared.utilities.pikaterDatabase.tests.DatabaseTest;

public class DatabaseInitialisation {
	
	private static final String dataSetPath="core/datasets";
	
	private void init() throws UserNotFoundException, FileNotFoundException, IOException, SQLException, ParseException{
		
		DatabaseTest dbTest=new DatabaseTest();
		
		this.createRolesAndUsers();
		dbTest.listUserAndRoles();
		
		this.createSampleResult();
		dbTest.listResults();
		
		this.createFileMapping();
		dbTest.listFileMappings();
		
		this.addWebDatasets();
		dbTest.listDataSets();
		
		this.insertFinishedBatch();
		dbTest.listBatches();
		
	}
	
	
	
	private void createSampleResult(){
		JPAResult res=new JPAResult();
		res.setAgentName("SampleAgent");
		res.setAgentTypeId(-1);
		res.setStart(new Date());
		
		DAOs.resultDAO.storeEntity(res);
	}
	
	private void addWebDatasets() throws FileNotFoundException, IOException, UserNotFoundException, SQLException{
		File dir=new File(DatabaseInitialisation.dataSetPath);
		
		JPAUser owner = DAOs.userDAO.getByLogin("stepan").get(0);
		System.out.println("Target user: "+owner.getLogin());
		
		File[] datasets=dir.listFiles();
		for(File datasetI : datasets){
			if(datasetI.isFile()){
				try{
				System.out.println("--------------------");
				System.out.println("Dataset: "+datasetI.getAbsolutePath());
				
				JPADataSetLO newDSLO=new JPADataSetLO();
				newDSLO.setCreated(new Date());
				newDSLO.setDescription(datasetI.getName());
				newDSLO.setOwner(owner);
				//hash a OID will be set using DAO
				DAOs.dataSetDAO.storeNewDataSet(datasetI, newDSLO);
				
				System.out.println("--------------------");
				System.out.println();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		
		
		
		///Update metadata
		
		
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
		
		
		
	}
	
	private void updateMetaDataForHash(File file) throws IOException{
		
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			System.out.println("DataSet not found");
		}
	}
	
	private void createRolesAndUsers() throws UserNotFoundException {		
		
		JPAUserPriviledge sdsPriv=new JPAUserPriviledge();
		sdsPriv.setName("SaveDataSet");
		DAOs.userPrivDAO.storeEntity(sdsPriv);

		
		JPAUserPriviledge sbPriv=new JPAUserPriviledge();
		sbPriv.setName("SaveBox");
		DAOs.userPrivDAO.storeEntity(sbPriv);
		
		JPARole u=new JPARole();
		u.setName("user");
		u.setDescription("Standard User Role");
		u.addPriviledge(sdsPriv);
		DAOs.roleDAO.storeEntity(u);
		
		
		JPARole a=new JPARole();
		a.setName("admin");
		a.setDescription("Admin Role");
		a.addPriviledge(sdsPriv);
		a.addPriviledge(sbPriv);
		DAOs.roleDAO.storeEntity(a);
		
		
		JPAUser u0=new JPAUser();
		u0.setLogin("zombie");
		u0.setPassword("xxx");
		u0.setEmail("invalid@mail.com");
		u0.setPriorityMax(-1);
		u0.setStatus(JPAUserStatus.PASSIVE);
		u0.setCreated(new Date());
		u0.setRole(u);
		DAOs.userDAO.storeEntity(u0);
		
		
		JPAUser u1=new JPAUser();
		u1.setLogin("stepan");
		u1.setPassword("123");
		u1.setEmail("Bc.Stepan.Balcar@gmail.com");
		u1.setPriorityMax(9);
		u1.setStatus(JPAUserStatus.ACTIVE);
		u1.setCreated(new Date());
		u1.setRole(a);
		DAOs.userDAO.storeEntity(u1);
		
		
		JPAUser u2=new JPAUser();
		u2.setLogin("kj");
		u2.setPassword("123");
		u2.setEmail("kj@gmail.com");
		u2.setPriorityMax(9);
		u2.setStatus(JPAUserStatus.ACTIVE);
		u2.setCreated(new Date());
		u2.setRole(a);
		DAOs.userDAO.storeEntity(u2);
	
		
		JPAUser u3=new JPAUser();
		u3.setLogin("sj");
		u3.setPassword("123");
		u3.setEmail("sj@gmail.com");
		u3.setPriorityMax(9);
		u3.setStatus(JPAUserStatus.ACTIVE);
		u3.setCreated(new Date());
		u3.setRole(a);
		DAOs.userDAO.storeEntity(u3);
		
		JPAUser u4=new JPAUser();
		u4.setLogin("sp");
		u4.setPassword("123");
		u4.setEmail("sp@gmail.com");
		u4.setPriorityMax(9);
		u4.setStatus(JPAUserStatus.ACTIVE);
		u4.setCreated(new Date());
		u4.setRole(a);
		DAOs.userDAO.storeEntity(u4);
		
		JPAUser u5=new JPAUser();
		u5.setLogin("martin");
		u5.setPassword("123");
		u5.setEmail("Martin.Pilat@mff.cuni.cz");
		u5.setPriorityMax(9);
		u5.setStatus(JPAUserStatus.ACTIVE);
		u5.setCreated(new Date());
		u5.setRole(u);
		DAOs.userDAO.storeEntity(u5);
	
		
		JPAUser u6=new JPAUser();
		u6.setLogin("klara");
		u6.setPassword("123");
		u6.setEmail("peskova@braille.mff.cuni.cz");
		u6.setPriorityMax(9);
		u6.setStatus(JPAUserStatus.ACTIVE);
		u6.setCreated(new Date());
		u6.setRole(u);
		DAOs.userDAO.storeEntity(u6);
	}
	
	private void createFileMapping() {
		
        File dir=new File(DatabaseInitialisation.dataSetPath);
		System.out.println(dir.getAbsolutePath());
		JPAUser owner = DAOs.userDAO.getByLogin("stepan").get(0);
		System.out.println("Target user: "+owner.getLogin());
		
		File[] datasets=dir.listFiles();
		for(File datasetI : datasets){
			if(datasetI.isFile()){
				try{
				System.out.println("--------------------");
				System.out.println("FileMapping for Dataset: "+datasetI.getAbsolutePath());
				
				String hash=Hash.getMD5Hash(datasetI);
				
				JPAFilemapping f = new JPAFilemapping();
				f.setUser(owner);
				f.setExternalfilename(datasetI.getName());
				f.setInternalfilename(hash);
				DAOs.filemappingDAO.storeEntity(f);
				
				System.out.println("--------------------");
				System.out.println();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		

	}
	
	private void insertFinishedBatch() throws ParseException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JPAResult result = new JPAResult();
		result.setAgentName("RBFNetwork");
		result.setAgentTypeId(0);
		result.setErrorRate(0.214285716414452);
		result.setFinish(new Date());
		result.setKappaStatistic(0.511627912521362);
		result.setMeanAbsoluteError(0.264998614788055);
		result.setNote("Note of result :-)");
		result.setOptions("-S 0 -M 0.2 ");
		result.setRelativeAbsoluteError(55.6497116088867);
		result.setRootMeanSquaredError(0.462737262248993);
		result.setRootRelativeSquaredError(93.7923049926758);
		result.setSerializedFileName("");
		result.setStart(dateFormat.parse("2014-03-29 11:06:55"));
		
		JPAExperiment experiment = new JPAExperiment();
		experiment.setStatus(JPAExperimentStatus.FINISHED);
		experiment.addResult(result);

		JPABatch batch = new JPABatch();
		batch.setName("Stepan's batch of experiments - school project");
		batch.setPriority(99);
		batch.addExperiment(experiment);
		batch.setCreated(new Date());

		DAOs.batchDAO.storeEntity(batch);
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
