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

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.ConnectionProvider;
import org.pikater.shared.utilities.pikaterDatabase.Database;
import org.pikater.shared.utilities.pikaterDatabase.io.PostgreLargeObjectReader;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.subtrees.dataSource.GetFile;
import org.pikater.core.ontology.subtrees.messages.*;
import org.pikater.core.ontology.subtrees.metadata.GetAllMetadata;
import org.pikater.core.ontology.subtrees.metadata.GetMetadata;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.postgresql.PGConnection;

import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

public class Agent_DataManager_New extends PikaterAgent {
	private final String DEFAULT_CONNECTION_PROVIDER = "defaultConnection";
	private static final String CONNECTION_ARG_NAME = "connection";
	private String connectionBean;
	private ConnectionProvider connectionProvider;
	private static final long serialVersionUID = 1L;
	
	private Database database=null;

	public static String dataPath = "core" + System.getProperty("file.separator") + "data" + System.getProperty("file.separator") + "files" + System.getProperty("file.separator");

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
			
			this.database=new Database(emf,(PGConnection)connectionProvider.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mt) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
				try {
					Action a = (Action) getContentManager().extractContent(request);

					//Maybe it's not needed
					//if (a.getAction() instanceof GetFileInfo) {
					//	return RespondToGetFileInfo(request, a);
					//}
					//Maybe it's not needed
					//if (a.getAction() instanceof GetFiles) {
					//	return RespondToGetFiles(request, a);
					//}
					//we have no external results and if any we wo't load them via this agent
					//if (a.getAction() instanceof LoadResults) {
					
					//we have already computed metadata 
					//if (a.getAction() instanceof SaveMetadata) {
					
					//we have already computed metadata and these dont change
					//if (a.getAction() instanceof UpdateMetadata) {
					
					//we didn't have anything for LoadREsults
					
					if (a.getAction() instanceof SaveResults) {
						return RespondToSaveResults(request, a);
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
					if (a.getAction() instanceof DeleteTempFiles) {
						return RespondToDeleteTempFiles(request);
					}
					if (a.getAction() instanceof GetFile) {
						return RespondToGetFile(request, a);
					}
					//no use anymore, we have JPA, but log the query
					if (a.getAction() instanceof ShutdownDatabase) {
						return RespondToShutdownDatabase(request);
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
			PostgreLargeObjectReader reader = database.getLargeObjectReader(hash);
			File temp = new File(dataPath + "temp" + System.getProperty("file.separator") + hash);
			FileOutputStream out = new FileOutputStream(temp);
			try {
				byte[] buf = new byte[100*1024];
				int read;
				while ((read = reader.read(buf, 0, buf.length)) > 0) {
					out.write(buf, 0, read);
				}
				temp.renameTo(new File(dataPath + hash));
			} finally {
				reader.close();
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
	
	private ACLMessage RespondToSaveResults(ACLMessage request, Action a) throws SQLException, ClassNotFoundException {
		SaveResults sr = (SaveResults) a.getAction();
		Task res = sr.getTask();

		EntityManager entityManager = emf.createEntityManager();

		try {
			entityManager.getTransaction().begin();
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


			for (Eval next_eval : res.getResult().getEvaluations() ) {
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

			entityManager.persist(jparesult);
			entityManager.getTransaction().commit();
			log("persisted a JPAResult");
		} finally {
			if (entityManager.getTransaction().isActive())
				entityManager.getTransaction().rollback();
			entityManager.close();
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

	

	private ACLMessage RespondToGetAllMetadata(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetAllMetadata gm = (GetAllMetadata) a.getAction();

		
		
		//metadata  w./wo. results     - exceptions
		
		// for now it really returns all metadata
		
		/**
		
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

**/
		List allMetadata = new ArrayList();
		
		log("Retrieving all of Global Metadata for DataSets...");
		java.util.List<JPADataSetLO> dslos=database.getAllDataSetLargeObjects();
		
		for(JPADataSetLO dslo:dslos){
			Metadata m = new Metadata();
			m.setDefault_task(dslo.getGlobalMetaData().getDefaultTaskType().getName());
			m.setNumber_of_attributes(dslo.getAttributeMetaData().size());
			m.setNumber_of_instances(dslo.getGlobalMetaData().getNumberofInstances());
			allMetadata.add(m);
		}

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result _result = new Result(a.getAction(), allMetadata);
		getContentManager().fillContent(reply, _result);

		return reply;
	}

	private ACLMessage RespondToGetTheBestAgent(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		/**GetTheBestAgent g = (GetTheBestAgent) a.getAction();
		
		String hash=g.getHash();
		
		java.util.List<JPAResult> res = database.getResultsByDataSetHash(hash);

		if(res.size()<=0){
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent("There are no results for this file in the database.");
			return reply;
		}
		
		JPAResult best=new JPAResult();
		best.setErrorRate(Double.MAX_VALUE);
		
		for(JPAResult r:res){
			if(r.getErrorRate()<best.getErrorRate()){
				best=r;
			}
		}

		Agent agent = new Agent();
		agent.setName(best.getAgentName());
		agent.setType(""+best.getAgentTypeId());
		agent.setOptions(agent.stringToOptions(best.getOptions()));
		agent.setGui_id(""+best.getErrorRate());

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result _result = new Result(a.getAction(), agent);
		getContentManager().fillContent(reply, _result);

		return reply;
		**/
		return null;
	}

	/**
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
**/
	/**
	private ACLMessage RespondToGetFiles(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetFiles gf = (GetFiles) a.getAction();

		String query = "SELECT * FROM jpafilemapping WHERE userid = " + gf.getUserID();

		log("Executing query: " + query);

		openDBConnection();
		Statement stmt = db.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		ArrayList files = new ArrayList();

		while (rs.next()) {
			files.add(rs.getString("externalFilename"));
		}

		Result r = new Result(a.getAction(), files);
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		getContentManager().fillContent(reply, r);

		db.close();
		return reply;
	}

**/
	private ACLMessage RespondToShutdownDatabase(ACLMessage request) throws SQLException, ClassNotFoundException {
		
		log("Database SHUTDOWN message received...");
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}

	private ACLMessage RespondToDeleteTempFiles(ACLMessage request) {
		String path = Agent_DataManager_New.dataPath + "temp" + System.getProperty("file.separator");

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
	
	private ACLMessage ReplyToGetMetadata(ACLMessage request, Action a) throws SQLException, ClassNotFoundException, CodecException, OntologyException {
		GetMetadata gm = (GetMetadata) a.getAction();

		JPADataSetLO dslo=database.getSingleDataSetByHash(gm.getHash());
		
		JPAGlobalMetaData globm=dslo.getGlobalMetaData();
		
		Metadata m = new Metadata();
		m.setDefault_task(globm.getDefaultTaskType().getName());
		m.setNumber_of_attributes(dslo.getAttributeMetaData().size());
		m.setNumber_of_instances(globm.getNumberofInstances());
		
		log("Retrieving metadata for (hash) :"+dslo.getHash());

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		Result _result = new Result(a.getAction(), m);
		getContentManager().fillContent(reply, _result);

		return reply;
	}
}