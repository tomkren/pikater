package org.pikater.core.agents.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jade.content.AgentAction;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.metadata.MetadataReader;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.subtrees.dataInstance.DataInstances;
import org.pikater.core.ontology.subtrees.metadata.Metadata;
import org.pikater.core.ontology.subtrees.metadata.NewDataset;
import org.pikater.core.ontology.subtrees.metadata.SaveMetadata;
import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.daos.AbstractDAO.EmptyResultAction;
import org.pikater.shared.database.pglargeobject.PostgreLargeObjectReader;
import org.pikater.shared.database.pglargeobject.PostgreLobAccess;

import weka.core.Instances;

public class Agent_MetadataQueen extends PikaterAgent {

	private static final long serialVersionUID = -1886699589066832983L;
	
	@Override
	public java.util.List<Ontology> getOntologies() {
		java.util.List<Ontology> ontologies = new java.util.ArrayList<Ontology>();
		ontologies.add(MetadataOntology.getInstance());		
		return ontologies;
	}
	
    @Override
    protected void setup() {
        initDefault();

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		addBehaviour(new AchieveREResponder(this, mt) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {
				try {
					Action a = (Action) getContentManager().extractContent(request);
					if (a.getAction() instanceof NewDataset) {
						return respondToNewDataset(request, a);
					}
				} catch (OntologyException e) {
					logError("Problem extracting content",e);
				} catch (CodecException e) {
					logError("Codec problem",e);
				} catch (Exception e) {
					logError("General Exception", e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logError("Failure responding to request: " + request.getContent());
				return failure;
			}

		});
        
    }
    
    private ACLMessage respondToNewDataset(ACLMessage request, Action a) {
        NewDataset nd=(NewDataset)a.getAction();
        
        int dataSetID=nd.getDataSetID();
        
        try {
			JPADataSetLO dslo= DAOs.dataSetDAO.getByID(dataSetID, EmptyResultAction.THROW);
			PostgreLargeObjectReader plor = PostgreLobAccess.getPostgreLargeObjectReader(dslo.getOID());
			
			File file=new File("core/freddie/"+dslo.getHash());
			if(!file.exists()){
			FileWriter fw=new FileWriter(file);
			
			char[] buf=new char[100];
			int s=-1;
			while((s=plor.read(buf))!=-1){
				fw.write(buf,0,s);
			}
			fw.close();
			}
			
			Metadata resultMetaData=this.readFile(file);
		
			this.sendSaveMetaDataRequest(resultMetaData, dataSetID);
				
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.INFORM);
		
			return reply;	
		} catch (NoResultException e) {
			logError("DataSet with ID "+dataSetID+" not found  in the database",e);
		} catch (IOException e) {
			logError("IOError while accessing dataset", e);
		}
        
		return null;
	}
    
    
    private void sendSaveMetaDataRequest(Metadata metadata,int dataSetID){
        AID receiver = new AID(AgentNames.DATA_MANAGER, false);

        SaveMetadata saveMetaDataAction = new SaveMetadata();
        saveMetaDataAction.setMetadata(metadata);
        saveMetaDataAction.setDataSetID(dataSetID);
        
        try {
            ACLMessage request = makeActionRequest(receiver, saveMetaDataAction);
            log("Sending SaveMetaDataAction to DataManager");
            ACLMessage reply = FIPAService.doFipaRequestClient(this, request, 10000);
            if (reply == null)
                logError("Reply not received");
            else
                log("Reply received: "+ACLMessage.getPerformative(reply.getPerformative())+" "+reply.getContent());
        } catch (CodecException | OntologyException e) {
            logError("Ontology/codec error occurred: ",e);
        } catch (FIPAException e) {
            logError("FIPA error occurred",e);
        }
    }
  
  
   /** Naplni pozadavek na konkretni akci pro jednoho ciloveho agenta */
    private ACLMessage makeActionRequest(AID target, AgentAction action) throws CodecException, OntologyException {
    	Ontology ontology = MetadataOntology.getInstance();
    	
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(target);
        msg.setLanguage(getCodec().getName());
        msg.setOntology(ontology.getName());
        getContentManager().fillContent(msg, new Action(target, action));
        return msg;
    }
  
    private Metadata readFile(File file) throws FileNotFoundException, IOException{	
			DataInstances data=new DataInstances();
			data.fillWekaInstances(new Instances(new BufferedReader(new FileReader(file))));
			MetadataReader reader=new MetadataReader();
			return reader.computeMetadata(data);
	}

}
