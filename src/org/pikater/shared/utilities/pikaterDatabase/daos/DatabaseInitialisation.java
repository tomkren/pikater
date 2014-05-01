package org.pikater.shared.utilities.pikaterDatabase.daos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.postgresql.PGConnection;
import org.pikater.core.agents.system.Agent_DataManager;
import org.pikater.shared.database.jpa.ExperimentStatus;
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
import org.pikater.shared.database.jpa.UserStatus;
import org.pikater.shared.database.PostgreSQLConnectionProvider;
import org.pikater.shared.utilities.pikaterDatabase.exceptions.UserNotFoundException;
import org.pikater.shared.utilities.pikaterDatabase.initialisation.JPAMetaDataReader;

public class DatabaseInitialisation {

	public enum PasswordChangeResult{Success,Error};
	
	PGConnection connection;
	EntityManagerFactory emf=null;
	EntityManager em = null;
	
	public DatabaseInitialisation(EntityManagerFactory emf,PGConnection connection){
		this.emf=emf;
		this.connection=connection;
	}

	
	/**
	 * Initialisation of test-data in DataBase
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws UserNotFoundException 
	 * @throws ParseException 
	 */
	private void itialisationData() throws SQLException, IOException, UserNotFoundException, ParseException{				
		
		this.createRolesAndUsers();
		this.testUser();
		
		//this.addWebDatasets();
		
		/**

		this.insertFinishedBatch();
		
		this.createFileMapping();
		**/
	}
	
	private void addWebDatasets() throws FileNotFoundException, IOException, UserNotFoundException, SQLException{
		File dir=new File(Agent_DataManager.datasetsPath);
		
		JPAUser user = DAOs.userDAO.getByLogin("stepan").get(0);
		System.out.println("Target user: "+user.getLogin());
		
		/**
		File[] datasets=dir.listFiles();
		for(File datasetI : datasets){
			if(datasetI.isFile()){
				try{
				System.out.println("--------------------");
				System.out.println("Dataset: "+datasetI.getAbsolutePath());
				JPAMetaDataReader mdr=new JPAMetaDataReader(database);
				mdr.readFile(datasetI);		
				System.out.println("MD5 hash: "+database.getMD5Hash(datasetI));
			
				JPADataSetLO dslo=database.saveDataSet(
					user,
					datasetI,
					datasetI.getName(),
					mdr.getJPAGlobalMetaData(),
					mdr.getJPAAttributeMetaData()
					);
				System.out.println(dslo);
				System.out.println("--------------------");
				System.out.println();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		**/
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
		u0.setStatus(UserStatus.PASSIVE);
		u0.setCreated(new Date());
		u0.setRole(u);
		DAOs.userDAO.storeEntity(u0);
		
		
		JPAUser u1=new JPAUser();
		u1.setLogin("stepan");
		u1.setPassword("123");
		u1.setEmail("Bc.Stepan.Balcar@gmail.com");
		u1.setPriorityMax(9);
		u1.setStatus(UserStatus.ACTIVE);
		u1.setCreated(new Date());
		u1.setRole(a);
		DAOs.userDAO.storeEntity(u1);
		
		
		JPAUser u2=new JPAUser();
		u2.setLogin("kj");
		u2.setPassword("123");
		u2.setEmail("kj@gmail.com");
		u2.setPriorityMax(9);
		u2.setStatus(UserStatus.ACTIVE);
		u2.setCreated(new Date());
		u2.setRole(a);
		DAOs.userDAO.storeEntity(u2);
	
		
		JPAUser u3=new JPAUser();
		u3.setLogin("sj");
		u3.setPassword("123");
		u3.setEmail("sj@gmail.com");
		u3.setPriorityMax(9);
		u3.setStatus(UserStatus.ACTIVE);
		u3.setCreated(new Date());
		u3.setRole(a);
		DAOs.userDAO.storeEntity(u3);
		
		JPAUser u4=new JPAUser();
		u4.setLogin("sp");
		u4.setPassword("123");
		u4.setEmail("sp@gmail.com");
		u4.setPriorityMax(9);
		u4.setStatus(UserStatus.ACTIVE);
		u4.setCreated(new Date());
		u4.setRole(a);
		DAOs.userDAO.storeEntity(u4);
		
		JPAUser u5=new JPAUser();
		u5.setLogin("martin");
		u5.setPassword("123");
		u5.setEmail("Martin.Pilat@mff.cuni.cz");
		u5.setPriorityMax(9);
		u5.setStatus(UserStatus.ACTIVE);
		u5.setCreated(new Date());
		u5.setRole(u);
		DAOs.userDAO.storeEntity(u5);
	
		
		JPAUser u6=new JPAUser();
		u6.setLogin("klara");
		u6.setPassword("123");
		u6.setEmail("peskova@braille.mff.cuni.cz");
		u6.setPriorityMax(9);
		u6.setStatus(UserStatus.ACTIVE);
		u6.setCreated(new Date());
		u6.setRole(u);
		DAOs.userDAO.storeEntity(u6);
	}
	
	public void testUser(){
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
	
	private void p(String s){
		System.out.println(s);
	}
	
	private void insertFinishedBatch() throws ParseException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JPAResult result = new JPAResult();
		result.setAgentName("RBFNetwork");
		result.setAgentTypeId(0);
		result.setErrorRate(0.214285716414452);
		result.setFinish(dateFormat.parse("2014-03-29 11:06:57"));
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
		experiment.setStatus(ExperimentStatus.FINISHED);
		experiment.addResult(result);

		JPABatch batch = new JPABatch();
		batch.setName("Stepan's batch of experiments - school project");
		batch.setPriority(99);
		batch.addExperiment(experiment);

		//this.database.persist(batch);
	}
	


	public static void main(String[] args) throws SQLException, IOException, UserNotFoundException, ClassNotFoundException, ParseException {

		EntityManagerFactory emf=null;//Persistence.createEntityManagerFactory("pikaterDataModel");

		DatabaseInitialisation data = new DatabaseInitialisation(
				emf,(PGConnection)(
						new PostgreSQLConnectionProvider(
								"jdbc:postgresql://localhost:5432/pikater",
								"postgres",
								"kukacok").getConnection()));
		data.itialisationData();

		
		System.out.println("mm");
	}
}
