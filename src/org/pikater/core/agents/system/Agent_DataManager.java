package org.pikater.core.agents.system;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.pikater.shared.database.jpa.JPAFilemapping;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.ConnectionProvider;
import org.pikater.shared.utilities.logging.PikaterLogger;
import org.pikater.shared.utilities.pikaterDatabase.Database;
import org.pikater.shared.utilities.pikaterDatabase.daos.DAOs;
import org.pikater.shared.utilities.pikaterDatabase.io.PostgreLargeObjectReader;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.data.DataOntology;
import org.pikater.core.ontology.data.GetFile;
import org.pikater.core.ontology.messages.*;
import org.postgresql.PGConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

public class Agent_DataManager extends PikaterAgent {

	private final String DEFAULT_CONNECTION_PROVIDER = "defaultConnection";
	private static final String CONNECTION_ARG_NAME = "connection";
	private String connectionBean;
	private ConnectionProvider connectionProvider;
	private static final long serialVersionUID = 1L;
	Connection db;

	public static String dataFilesPath =
			"core" + System.getProperty("file.separator") +
			"data" + System.getProperty("file.separator") +
			"files" + System.getProperty("file.separator");
	public static String datasetsPath =
			"core" + System.getProperty("file.separator") +
			"data" + System.getProperty("file.separator");
	
	@Override
	protected void setup() {
		try {
			initDefault();
			registerWithDF();
			getContentManager().registerOntology(DataOntology.getInstance());

			if (containsArgument(CONNECTION_ARG_NAME)) {
				connectionBean = getArgumentValue(CONNECTION_ARG_NAME);
			} else {
				connectionBean = DEFAULT_CONNECTION_PROVIDER;
			}
			connectionProvider = (ConnectionProvider) context.getBean(connectionBean);

			log("Connecting to " + connectionProvider.getConnectionInfo() + ".");
			openDBConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		File data = new File(dataFilesPath + "temp");
		if (!data.exists()) {
			log("Creating directory: " + Agent_DataManager.dataFilesPath);
			if (data.mkdirs()) {
				log("Succesfully created directory: " + dataFilesPath);
			} else {
				logError("Error creating directory: " + dataFilesPath);
			}
		}

		try {
			db.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mt) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
				try {
					Action a = (Action) getContentManager().extractContent(request);

					if (a.getAction() instanceof ImportFile) {
						return RespondToImportFile(request, a);
					}
					if (a.getAction() instanceof TranslateFilename) {
						return RespondToTranslateFilename(request, a);
					}
					if (a.getAction() instanceof SaveResults) {
						return RespondToSaveResults(request, a);
					}
					if (a.getAction() instanceof SaveMetadata) {
						return RespondToGetAclMessage(request, a);
					}
					if (a.getAction() instanceof GetMetadata) {
						return ReplyToGetMetadata(request, a);
					}
					if (a.getAction() instanceof GetAllMetadata) {
						return RespondToGetAllMetadata(request, a);
					}
					if (a.getAction() instanceof GetTheBestAgent) {
						return RespondToGetTheBestAgent(request, a);
					}
					if (a.getAction() instanceof GetFileInfo) {
						return RespondToGetFileInfo(request, a);
					}
					if (a.getAction() instanceof UpdateMetadata) {
						return ReplyToUpdateMetadata(request, a);
					}
					if (a.getAction() instanceof GetFiles) {
						return RespondToGetFiles(request, a);
					}
					if (a.getAction() instanceof LoadResults) {
						return RespondToLoadResults(request, a);
					}
					if (a.getAction() instanceof DeleteTempFiles) {
						return RespondToDeleteTempFiles(request);
					}
					if (a.getAction() instanceof ShutdownDatabase) {
						return RespondToShutdownDatabase(request);
					}
					if (a.getAction() instanceof GetFile) {
						return RespondToGetFile(request, a);
					}
				} catch (OntologyException e) {
					e.printStackTrace();
					logError("Problem extracting content: " + e.getMessage());
				} catch (CodecException e) {
					e.printStackTrace();
					logError("Codec problem: " + e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
					logError("SQL error: " + e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logError("Failure responding to request: " + request.getContent());
				return failure;
			}
		});

	}

	private ACLMessage RespondToGetFile(ACLMessage request, Action a) throws CodecException, OntologyException, ClassNotFoundException, SQLException {
		String hash = ((GetFile)a.getAction()).getHash();

		try {
			openDBConnection();
			PostgreLargeObjectReader reader = new Database(emf, (PGConnection) db).getLargeObjectReader(hash);
			File temp = new File(dataFilesPath + "temp" + System.getProperty("file.separator") + hash);
			FileOutputStream out = new FileOutputStream(temp);
			try {
				byte[] buf = new byte[100*1024];
				int read;
				while ((read = reader.read(buf, 0, buf.length)) > 0) {
					out.write(buf, 0, read);
				}
				temp.renameTo(new File(dataFilesPath + hash));
			} finally {
				db.close();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		Result r = new Result(a, "OK");
		getContentManager().fillContent(reply, r);

		return reply;
	}

	/************************************************************************************************
	 * Obsolete methods
	 * 
	 */
	private ACLMessage RespondToImportFile(ACLMessage request, Action a) throws IOException, CodecException, OntologyException, SQLException, ClassNotFoundException {
		ImportFile im = (ImportFile) a.getAction();

		String pathPrefix = dataFilesPath + "temp" + System.getProperty("file.separator");

		if (im.isTempFile()) {

			FileWriter fw = new FileWriter(pathPrefix + im.getExternalFilename());
			fw.write(im.getFileContent());
			fw.close();

			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.INFORM);

			Result r = new Result(im, pathPrefix + im.getExternalFilename());
			getContentManager().fillContent(reply, r);

			return reply;

		}

		if (im.getFileContent() == null) {
			String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "core" + System.getProperty("file.separator");
			path += "incoming" + System.getProperty("file.separator") + im.getExternalFilename();

			String internalFilename = md5(path);

			
			/**
			 * CREATE??? a new DataSet with empty metadata
			 */
			emptyMetadataToDB(internalFilename, im.getExternalFilename());

			File f = new File(path);

			if(DAOs.filemappingDAO.fileExists(internalFilename)){
				f.delete();
				PikaterLogger.getLogger(Agent_DataManager.class).warn("File " + internalFilename + " already present in the database");
			}else{
				JPAFilemapping fm=new JPAFilemapping();
				fm.setUser(DAOs.userDAO.getByID(im.getUserID()).get(0));
				fm.setInternalfilename(internalFilename);
				fm.setExternalfilename(im.getExternalFilename());
				
				DAOs.filemappingDAO.storeEntity(fm);

				String newName = Agent_DataManager.dataFilesPath + internalFilename;
				move(f, new File(newName));
			}
			
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.INFORM);

			Result r = new Result(im, internalFilename);
			getContentManager().fillContent(reply, r);

			return reply;
		} else {

			String fileContent = im.getFileContent();
			String fileName = im.getExternalFilename();
			String internalFilename = DigestUtils.md5Hex(fileContent);

			emptyMetadataToDB(internalFilename, fileName);


			if(DAOs.filemappingDAO.fileExists(internalFilename)){
				PikaterLogger.getLogger(Agent_DataManager.class).warn("File " + internalFilename + " already present in the database");
			}else{
				JPAFilemapping fm=new JPAFilemapping();
				fm.setUser(DAOs.userDAO.getByID(im.getUserID()).get(0));
				fm.setInternalfilename(internalFilename);
				fm.setExternalfilename(im.getExternalFilename());
				
				DAOs.filemappingDAO.storeEntity(fm);

				String newName = Agent_DataManager.dataFilesPath + internalFilename;

				FileWriter file = new FileWriter(newName);
				file.write(fileContent);
				file.close();

				PikaterLogger.getLogger(Agent_DataManager.class).info("Created file: " + newName);
			}

			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.INFORM);

			Result r = new Result(im, internalFilename);
			getContentManager().fillContent(reply, r);

			db.close();
			return reply;
		}
	}

	private ACLMessage RespondToTranslateFilename(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		TranslateFilename tf = (TranslateFilename) a.getAction();
		
		java.util.List<JPAFilemapping> files=null;
		
		
		String internalFilename = "error";
		
		if (tf.getInternalFilename() == null) {
			files=DAOs.filemappingDAO.getByUserIDandExternalFilename(tf.getUserID(), tf.getExternalFilename());
			if(files.size()>0){
				internalFilename=files.get(0).getInternalfilename();
			} else {
				String pathPrefix = dataFilesPath + "temp" + System.getProperty("file.separator");

				String tempFileName = pathPrefix + tf.getExternalFilename();
				if (new File(tempFileName).exists())
					internalFilename = "temp" + System.getProperty("file.separator") + tf.getExternalFilename();
			}
			
		} else {
			files=DAOs.filemappingDAO.getByUserIDandInternalFilename(tf.getUserID(), tf.getInternalFilename());
			if(files.size()>0){
				internalFilename=files.get(0).getExternalfilename();
			} else {
				String pathPrefix = dataFilesPath + "temp" + System.getProperty("file.separator");

				String tempFileName = pathPrefix + tf.getExternalFilename();
				if (new File(tempFileName).exists())
					internalFilename = "temp" + System.getProperty("file.separator") + tf.getExternalFilename();
			}
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		Result r = new Result(tf, internalFilename);
		getContentManager().fillContent(reply, r);

		return reply;
	}

	private ACLMessage RespondToSaveResults(ACLMessage request, Action a) throws SQLException, ClassNotFoundException {
		SaveResults sr = (SaveResults) a.getAction();
		Task res = sr.getTask();
	
		try {
			JPAResult jparesult = new JPAResult();

			jparesult.setAgentName(res.getAgent().getName());
			// nesedi na novy model kde jsou ciselne ID agentu - stary model
			// pouziva Stringy...
			// jparesult.setAgentTypeId(res.getAgent().getType());
			jparesult.setOptions(res.getAgent().optionsToString());
			// cim se ma naplnit serializedFilename?
			// co tyhle hodnoty co v novem model nejsou?
			// query += "\'" +
			// res.getData().removePath(res.getData().getTrain_file_name()) +
			// "\',";
			// query += "\'" +
			// res.getData().removePath(res.getData().getTest_file_name()) +
			// "\',";

			float Error_rate = Float.MAX_VALUE;
			float Kappa_statistic = Float.MAX_VALUE;
			float Mean_absolute_error = Float.MAX_VALUE;
			float Root_mean_squared_error = Float.MAX_VALUE;
			float Relative_absolute_error = Float.MAX_VALUE; // percent
			float Root_relative_squared_error = Float.MAX_VALUE; // percent
			int duration = Integer.MAX_VALUE; // miliseconds
			float durationLR = Float.MAX_VALUE;

			Iterator itr = res.getResult().getEvaluations().iterator();
			while (itr.hasNext()) {
				Eval next_eval = (Eval) itr.next();
				if (next_eval.getName().equals("error_rate")) {
					Error_rate = next_eval.getValue();
				}

				if (next_eval.getName().equals("kappa_statistic")) {
					Kappa_statistic = next_eval.getValue();
				}

				if (next_eval.getName().equals("mean_absolute_error")) {
					Mean_absolute_error = next_eval.getValue();
				}

				if (next_eval.getName().equals("root_mean_squared_error")) {
					Root_mean_squared_error = next_eval.getValue();
				}

				if (next_eval.getName().equals("relative_absolute_error")) {
					Relative_absolute_error = next_eval.getValue();
				}

				if (next_eval.getName().equals("root_relative_squared_error")) {
					Root_relative_squared_error = next_eval.getValue();
				}

				if (next_eval.getName().equals("duration")) {
					duration = (int) next_eval.getValue();
				}
				if (next_eval.getName().equals("durationLR")) {
					durationLR = (float) next_eval.getValue();
				}
			}

			String start = getDateTime();
			String finish = getDateTime();
			
			if (res.getStart() != null) { start = res.getStart(); }
			if (res.getFinish() != null){ finish = res.getFinish(); }
			
			jparesult.setErrorRate(Error_rate);
			jparesult.setKappaStatistic(Kappa_statistic);
			jparesult.setMeanAbsoluteError(Mean_absolute_error);
			jparesult.setRootMeanSquaredError(Root_mean_squared_error);
			jparesult.setRelativeAbsoluteError(Relative_absolute_error);
			jparesult.setRootRelativeSquaredError(Root_relative_squared_error);
			jparesult.setStart(new Date(Timestamp.valueOf(start).getTime()));
			// query += "\'" + Timestamp.valueOf(res.getStart()) + "\',";
			jparesult.setFinish(new Date(Timestamp.valueOf(finish).getTime()));
			// query += "\'" + Timestamp.valueOf(res.getFinish()) + "\',";

			// v novem modelu tohle neni
			// query += "\'" + duration + "\',";
			// query += "\'" + durationLR + "\',";

			// je to ono?
			jparesult.setSerializedFileName(res.getResult().getObject_filename());
			// query += "\'" + res.getResult().getObject_filename() + "\', ";

			// jparesult.setExperiment(TODO);
			// query += "\'" + res.getId().getIdentificator() + "\',"; // TODO -
			// pozor - neni jednoznacne, pouze pro jednoho managera
			// query += "\'" + res.getProblem_name() + "\',";
			jparesult.setNote(res.getNote());
			
			DAOs.resultDAO.storeEntity(jparesult);
			PikaterLogger.getLogger(Agent_DataManager.class.getCanonicalName()).info("Persisted JPAResult");
		}catch(Exception e){
			PikaterLogger.getLogger(Agent_DataManager.class.getCanonicalName()).error("Error in SaveResults", e);;
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}
	
	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
		Date date = new Date();

		return dateFormat.format(date);
    }

	

	private ACLMessage RespondToGetAclMessage(ACLMessage request, Action a) throws SQLException, ClassNotFoundException {
		SaveMetadata saveMetadata = (SaveMetadata) a.getAction();
		Metadata metadata = saveMetadata.getMetadata();

		openDBConnection();
		Statement stmt = db.createStatement();

		String query = "UPDATE metadata SET ";
		query += "numberOfInstances=" + metadata.getNumber_of_instances() + ", ";
		query += "numberOfAttributes=" + metadata.getNumber_of_attributes() + ", ";
		query += "missingValues=" + metadata.getMissing_values();
		if (metadata.getAttribute_type() != null) {
			query += ", attributeType=\'" + metadata.getAttribute_type() + "\' ";
		}
		if (metadata.getDefault_task() != null) {
			query += ", defaultTask=\'" + metadata.getDefault_task() + "\' ";
		}

		// the external file name contains part o the path
		// (db/files/name) -> split and use only the [2] part
		query += " WHERE internalFilename=\'" + metadata.getInternal_name().split(Pattern.quote(System.getProperty("file.separator")))[2] + "\'";

		log("Executing query: " + query);

		stmt.executeUpdate(query);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		db.close();
		return reply;
	}

	private ACLMessage RespondToGetAllMetadata(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetAllMetadata gm = (GetAllMetadata) a.getAction();

		openDBConnection();
		Statement stmt = db.createStatement();

		String query;
		if (gm.getResults_required()) {
			query = "SELECT * FROM metadata WHERE EXISTS " + "(SELECT * FROM results WHERE results.dataFile=metadata.internalFilename)";
			if (gm.getExceptions() != null) {
				Iterator itr = gm.getExceptions().iterator();
				while (itr.hasNext()) {
					Metadata m = (Metadata) itr.next();
					query += " AND ";
					query += "internalFilename <> '" + new File(m.getInternal_name()).getName() + "'";
				}
			}
			query += " ORDER BY externalFilename";
		} else {
			query = "SELECT * FROM metadata";

			if (gm.getExceptions() != null) {
				query += " WHERE ";
				boolean first = true;
				Iterator itr = gm.getExceptions().iterator();
				while (itr.hasNext()) {
					Metadata m = (Metadata) itr.next();
					if (!first) {
						query += " AND ";
					}
					query += "internalFilename <> '" + new File(m.getInternal_name()).getName() + "'";
					first = false;
				}

			}
			query += " ORDER BY externalFilename";
		}

		List allMetadata = new ArrayList();

		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			Metadata m = new Metadata();
			m.setAttribute_type(rs.getString("attributeType"));
			m.setDefault_task(rs.getString("defaultTask"));
			m.setExternal_name(rs.getString("externalFilename"));
			m.setInternal_name(rs.getString("internalFilename"));
			m.setMissing_values(rs.getBoolean("missingValues"));
			m.setNumber_of_attributes(rs.getInt("numberOfAttributes"));
			m.setNumber_of_instances(rs.getInt("numberOfInstances"));

			allMetadata.add(m);
		}

		log("Executing query: " + query);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result _result = new Result(a.getAction(), allMetadata);
		getContentManager().fillContent(reply, _result);

		db.close();
		return reply;
	}

	private ACLMessage RespondToGetTheBestAgent(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetTheBestAgent g = (GetTheBestAgent) a.getAction();
		String name = g.getNearest_file_name();

		openDBConnection();
		Statement stmt = db.createStatement();

		String query = "SELECT * FROM results " + "WHERE dataFile =\'" + name + "\'" + " AND errorRate = (SELECT MIN(errorRate) FROM results " + "WHERE dataFile =\'" + name + "\')";
		log("Executing query: " + query);

		ResultSet rs = stmt.executeQuery(query);
		if (!rs.isBeforeFirst()) {
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent("There are no results for this file in the database.");

			db.close();
			return reply;
		}
		rs.next();

		Agent agent = new Agent();
		agent.setName(rs.getString("agentName"));
		agent.setType(rs.getString("agentType"));
		agent.setOptions(agent.stringToOptions(rs.getString("options")));
		agent.setGui_id(rs.getString("errorRate"));

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result _result = new Result(a.getAction(), agent);
		getContentManager().fillContent(reply, _result);

		db.close();
		return reply;
	}

	private ACLMessage RespondToGetFileInfo(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetFileInfo gfi = (GetFileInfo) a.getAction();

		String query = "SELECT * FROM filemetadata WHERE " + gfi.toSQLCondition();

		openDBConnection();
		Statement stmt = db.createStatement();

		log("Executing query: " + query);

		ResultSet rs = stmt.executeQuery(query);

		List fileInfos = new ArrayList();

		while (rs.next()) {
			Metadata m = new Metadata();
			m.setAttribute_type(rs.getString("attributeType"));
			m.setDefault_task(rs.getString("defaultTask"));
			m.setExternal_name(rs.getString("externalFilename"));
			m.setInternal_name(rs.getString("internalFilename"));
			m.setMissing_values(rs.getBoolean("missingValues"));
			m.setNumber_of_attributes(rs.getInt("numberOfAttributes"));
			m.setNumber_of_instances(rs.getInt("numberOfInstances"));
			fileInfos.add(m);
		}

		Result r = new Result(a.getAction(), fileInfos);
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		getContentManager().fillContent(reply, r);

		db.close();
		return reply;
	}

	private ACLMessage RespondToGetFiles(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetFiles gf = (GetFiles) a.getAction();

		java.util.List<JPAFilemapping> userFiles=DAOs.filemappingDAO.getByUserID(gf.getUserID());
		
		ArrayList files = new ArrayList();

		for(JPAFilemapping fm:userFiles){
			files.add(fm.getExternalfilename());
		}

		Result r = new Result(a.getAction(), files);
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		getContentManager().fillContent(reply, r);

		db.close();
		return reply;
	}

	private ACLMessage RespondToShutdownDatabase(ACLMessage request) throws SQLException, ClassNotFoundException {
		
		PikaterLogger.getLogger(Agent_DataManager.class).warn("Database SHUTDOWN initiated in DataManager");

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}

	private ACLMessage RespondToDeleteTempFiles(ACLMessage request) {
		String path = this.dataFilesPath + "temp" + System.getProperty("file.separator");

		File tempDir = new File(path);
		String[] files = tempDir.list();

		if (files != null) {
			for (String file : files) {
				File d = new File(path + file);
				d.delete();
			}
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		return reply;
	}

	private ACLMessage RespondToLoadResults(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		LoadResults lr = (LoadResults) a.getAction();

		String query = "SELECT * FROM resultsExternal " + lr.asSQLCondition();
		log(query);

		openDBConnection();
		Statement stmt = db.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		ArrayList results = new ArrayList();

		while (rs.next()) {

			SavedResult sr = new SavedResult();

			sr.setAgentType(rs.getString("agentType"));
			sr.setAgentOptions(rs.getString("options"));
			sr.setTrainFile(rs.getString("trainFileExt"));
			sr.setTestFile(rs.getString("testFileExt"));
			sr.setErrorRate(rs.getDouble("errorRate"));
			sr.setKappaStatistic(rs.getDouble("kappaStatistic"));
			sr.setMeanAbsError(rs.getDouble("meanAbsoluteError"));
			sr.setRMSE(rs.getDouble("rootMeanSquaredError"));
			sr.setRootRelativeSquaredError(rs.getDouble("rootRelativeSquaredError"));
			sr.setRelativeAbsoluteError(rs.getDouble("relativeAbsoluteError"));
			sr.setDate("nodate");

			results.add(sr);
		}

		Result r = new Result(a.getAction(), results);
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		getContentManager().fillContent(reply, r);

		db.close();
		return reply;
	}

	private void openDBConnection() throws SQLException, ClassNotFoundException {
		db = connectionProvider.getConnection();
	}

	private void emptyMetadataToDB(String internalFilename, String externalFilename) throws SQLException, ClassNotFoundException {
		openDBConnection();
		Statement stmt = db.createStatement();

		String query = "SELECT COUNT(*) AS number FROM metadata WHERE internalFilename = \'" + internalFilename + "\'";
		String query1 = "SELECT COUNT(*) AS number FROM jpafilemapping WHERE internalFilename = \'" + internalFilename + "\'";

		log("Executing query " + query);
		log("Executing query " + query1);

		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		int isInMetadata = rs.getInt("number");

		ResultSet rs1 = stmt.executeQuery(query1);
		rs1.next();
		int isInFileMapping = rs1.getInt("number");

		if (isInMetadata == 0 && isInFileMapping == 1) {
			log("Executing query: " + query);
			query = "INSERT into metadata (externalFilename, internalFilename, defaultTask, " + "attributeType, numberOfInstances, numberOfAttributes, missingValues)" + "VALUES (\'"
					+ externalFilename + "\',\'" + internalFilename + "\', null, " + "null, 0, 0, false)";
			stmt.executeUpdate(query);
		}
		// stmt.close();
		db.close();
	}

	private ACLMessage ReplyToGetMetadata(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetMetadata gm = (GetMetadata) a.getAction();

		openDBConnection();
		Statement stmt = db.createStatement();

		String query = "SELECT * FROM metadata WHERE internalfilename = '" + gm.getInternal_filename() + "'";

		Metadata m = new Metadata();

		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			m.setAttribute_type(rs.getString("attributeType"));
			m.setDefault_task(rs.getString("defaultTask"));
			m.setExternal_name(rs.getString("externalFilename"));
			m.setInternal_name(rs.getString("internalFilename"));
			m.setMissing_values(rs.getBoolean("missingValues"));
			m.setNumber_of_attributes(rs.getInt("numberOfAttributes"));
			m.setNumber_of_instances(rs.getInt("numberOfInstances"));
		}

		log("Executing query: " + query);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result _result = new Result(a.getAction(), m);
		getContentManager().fillContent(reply, _result);

		db.close();
		return reply;
	}

	private ACLMessage ReplyToUpdateMetadata(ACLMessage request, Action a) throws SQLException, ClassNotFoundException {
		UpdateMetadata updateMetadata = (UpdateMetadata) a.getAction();
		Metadata metadata = updateMetadata.getMetadata();

		openDBConnection();
		Statement stmt = db.createStatement();

		String query = "UPDATE metadata SET ";
		query += "numberOfInstances=" + metadata.getNumber_of_instances() + ", ";
		query += "numberOfAttributes=" + metadata.getNumber_of_attributes() + ", ";
		query += "missingValues=" + metadata.getMissing_values() + "";
		if (metadata.getAttribute_type() != null) {
			query += ", attributeType=\'" + metadata.getAttribute_type() + "\' ";
		}
		if (metadata.getDefault_task() != null) {
			query += ", defaultTask=\'" + metadata.getDefault_task() + "\' ";
		}
		query += " WHERE internalFilename =\'" + metadata.getInternal_name() + "\'";

		log("Executing query: " + query);

		stmt.executeUpdate(query);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		db.close();
		return reply;
	}

	// Move file (src) to File/directory dest.
	public static synchronized void move(File src, File dest) throws FileNotFoundException, IOException {
		copy(src, dest);
		src.delete();
	}

	// Copy file (src) to File/directory dest.
	public static synchronized void copy(File src, File dest) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dest);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	private String md5(String path) {

		StringBuffer sb = null;

		try {
			FileInputStream fs = new FileInputStream(path);
			sb = new StringBuffer();

			int ch;
			while ((ch = fs.read()) != -1) {
				sb.append((char) ch);
			}
			fs.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logError("File not found: " + path + " -- " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logError("Error reading file: " + path + " -- " + e.getMessage());
		}

		String md5 = DigestUtils.md5Hex(sb.toString());

		log("MD5 hash of file " + path + " is " + md5);

		return md5;
	}
	/*********************************************************************
	 * End of obsolete methods
	 */
}