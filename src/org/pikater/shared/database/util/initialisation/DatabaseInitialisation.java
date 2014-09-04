package org.pikater.shared.database.util.initialisation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
import org.pikater.core.CoreConfiguration;
import org.pikater.shared.database.exceptions.UserNotFoundException;
import org.pikater.shared.database.jpa.JPAExternalAgent;
import org.pikater.shared.database.jpa.JPARole;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.JPAUserPriviledge;
import org.pikater.shared.database.jpa.PikaterPriviledge;
import org.pikater.shared.database.jpa.PikaterRole;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.util.CustomActionResultFormatter;
import org.pikater.shared.util.DateUtils;

public class DatabaseInitialisation {
	
	private static final String BEANS_CONFIGBASE="core/configbase/Beans.xml";
	private static final String PERSISTENCE_CONFIGBASE="core/configbase/persistence.xml";
	
	private static final String PERSISTENT_TARGET="META-INF/persistence.xml";
	
	
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
		
		JPARole a= new JPARole(PikaterRole.ADMIN.name(),PikaterRole.ADMIN);
		a.addPriviledge(sdsPriv);
		a.addPriviledge(sbPriv);
		
		DAOs.roleDAO.storeEntity(u);
		DAOs.roleDAO.storeEntity(a);
	}
	
	private void createAdminUser(String login,String password, String email){
		JPARole a = DAOs.roleDAO.getByPikaterRole(PikaterRole.ADMIN);
		JPAUser u=JPAUser.createAccountForDBInit(login,password, email, a);
		DAOs.userDAO.storeEntity(u);
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

	private String readWholeFile(String source) throws FileNotFoundException{
		Scanner scan = new Scanner(new File(source));  
		scan.useDelimiter("\\Z");  
		String text=scan.next();
		scan.close();
		return text;
	}
	
	private void generateConfigFile(String source, File output, String dbPath, String dbUser, String dbPassword) throws IOException{
		String sourceString=this.readWholeFile(source);
		sourceString = sourceString.replaceAll("###databaseURL###", dbPath);
		sourceString = sourceString.replaceAll("###databaseusername###", dbUser);
		sourceString = sourceString.replaceAll("###databasepassword###", dbPassword);
		FileWriter fw = new FileWriter(output);
		System.out.println("Writing file "+output.getAbsolutePath());
		try {
			fw.write(sourceString);
		} catch (IOException e) {
			System.err.println("Can't write output file: "+output.getAbsolutePath());
			e.printStackTrace();
		}finally{
			fw.close();
		}
		System.out.println("File "+output.getAbsolutePath()+" was successfully created");
	}
	
	private void configGeneration() throws IOException{
		p("Database URL in format (no leading slashes): host:port/databasename ");
		p("e.g. localhost:5432/mydatabase");
		String dbPath=br.readLine();
		p("Please add DB username: ");
		String dbUser=br.readLine();
		p("Please add password for DB user "+dbUser+":");
		String dbPassword=br.readLine();
		p("Generating configuration files for DB access. In next steps these files are used,");
		p("please make sure, they are in correct locations.");
		p("");
		p("persistence.xml current target (type new or leave blank) ");
		p("src"+File.separator+DatabaseInitialisation.PERSISTENT_TARGET);
		String persistenceTarget=br.readLine();
		if(persistenceTarget.equals(""))
			persistenceTarget="src"+File.separator+DatabaseInitialisation.PERSISTENT_TARGET;
				
		p("Beans.xml current target (type new or leave blank) ");
		p("src"+File.separator+CoreConfiguration.BEANS_CONFIG_FILE);
		String beansTarget=br.readLine();
		if(beansTarget.equals(""))
			beansTarget="src"+File.separator+CoreConfiguration.BEANS_CONFIG_FILE;
		
		p("Generating "+persistenceTarget+" ...");
		this.generateConfigFile(DatabaseInitialisation.PERSISTENCE_CONFIGBASE,
				new File(persistenceTarget),
				dbPath,
				dbUser,
				dbPassword);

		p("Generating "+beansTarget+" ...");
		this.generateConfigFile(DatabaseInitialisation.BEANS_CONFIGBASE,
				new File(beansTarget),
				dbPath,
				dbUser,
				dbPassword);
	}
	
	private void databaseConfiguration() throws IOException{
		p("Creating user roles...");
		this.createRoles();
		p("Add username of the first administrator: ");
		String login=br.readLine();
		p("Add password for user "+login+" : ");
		String pwd=br.readLine();
		p("Add e-mail address for user "+login+" : ");
		String email=br.readLine();
		this.createAdminUser(login, pwd, email);
		p("");
		p("Now administrator user should be stored in the database and ");
		p("you can start the web interface of Pikater and log in using ");
		p("this administrator login.");
		p("Other user accounts can be created using the web interface.");
	}
	
	private void configAll() throws IOException{
		this.configGeneration();
		this.databaseConfiguration();
	}
	
	private void runInstallation() throws IOException{
		p("--------------------------------------------------------------------------------");
		p("|                           WELCOME to PIKATER                                 |");
		p("--------------------------------------------------------------------------------");
		p("");
		p("Before you can run the system some configuration files must be generated.");
		p("These files are stored in plain text format, so make sure, that can't be read ");
		p("by anyone.");
		p("Also some default database entries will be generated, which contains the first ");
		p("user with administrator priviledge. Password of this user is stored as hash ");
		p("in the database.");
		p("");
		p("Please choose, which part of initialisation would you like to run:");
		p("Whole DB initialisation: 'a'");
		p("Config file generation : 'c'");
		p("Just DB initialisation : 'd'");
		String choice=br.readLine().toLowerCase();
		if(choice.equals("c")){
			this.configGeneration();
		}else if(choice.equals("d")){
			this.databaseConfiguration();
		}else if(choice.equals("a")){
			this.configAll();
		}else{
			p("Invalid choice...exiting.");
		}
			
	}
	
	private static BufferedReader br;

	public static void main(String[] args) throws SQLException, IOException, UserNotFoundException, ClassNotFoundException, ParseException {
		br=new BufferedReader(new InputStreamReader(System.in));
		DatabaseInitialisation data = new DatabaseInitialisation();
		data.runInstallation();
		System.out.println("End of Database Initialisation");
	}
}
