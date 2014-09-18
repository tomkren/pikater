package org.pikater.core.agents.system.data;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.subtrees.dataSource.DataSourcePath;
import org.pikater.core.ontology.subtrees.dataSource.GetDataSourcePath;
import org.pikater.core.ontology.subtrees.dataSource.RegisterDataSourceConcept;

/**
 * User: Kuba
 * Date: 2.5.2014
 * Time: 15:39
 */
public class AgentDataSourceCommunicator {
    PikaterAgent initAgent;
    AID receiverAid;

    public AgentDataSourceCommunicator(PikaterAgent initAgent, boolean localOnly) throws Exception {
        this.initAgent=initAgent;
        ServiceDescription sd = new ServiceDescription();
        sd.setType(AgentDataSource.SERVICE_TYPE);
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd);
        SearchConstraints constraints=new SearchConstraints();
        constraints.setMaxDepth(0L);
        constraints.setMaxResults(1L);
        try {
            DFAgentDescription[] searchResults= DFService.search(initAgent,dfd,constraints);
            if (searchResults.length==1)
            {
                receiverAid=searchResults[0].getName();
            }
            else
            {
                throw new Exception("No DataSourceAgent found");
            }
        } catch (FIPAException e) {
        	initAgent.logException(e.getMessage(), e);
        }
    }

    public String getDataSourceLocalPath(String taskId,String type) {
    	Ontology ontology = DataOntology.getInstance();
    	
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(receiverAid);
        msg.setLanguage(initAgent.getCodec().getName());
        msg.setOntology(ontology.getName());

        GetDataSourcePath getDataSourcePath = new GetDataSourcePath();
        getDataSourcePath.setTaskId(taskId);
        getDataSourcePath.setType(type);

        Action a = new Action(initAgent.getAID(),getDataSourcePath);

        try {
            initAgent.getContentManager().fillContent(msg, a);
            ACLMessage msg_reply = FIPAService.doFipaRequestClient(initAgent, msg);
            DataSourcePath result=(DataSourcePath)((Result)msg_reply.getContentObject()).getValue();
            return result.getPath();
        } catch (Codec.CodecException e) {
        	initAgent.logException(e.getMessage(), e);
        } catch (UnreadableException e) {
        	initAgent.logException(e.getMessage(), e);
        } catch (FIPAException e) {
        	initAgent.logException(e.getMessage(), e);
        } catch (OntologyException e) {
        	initAgent.logException(e.getMessage(), e);
        }
 
 
 
        return "failure";
    }

    public void registerDataSources(String taskId,String[] typeList) {
    	Ontology ontology = DataOntology.getInstance();
    	
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(receiverAid);
        msg.setLanguage(initAgent.getCodec().getName());
        msg.setOntology(ontology.getName());

        RegisterDataSourceConcept registerDS = new RegisterDataSourceConcept();
        registerDS.setTaskId(taskId);
        registerDS.setDataTypes(typeList);

        Action a = new Action(initAgent.getAID(),registerDS);

        try {
            initAgent.getContentManager().fillContent(msg, a);
            initAgent.send(msg);
        } catch (Codec.CodecException e) {
        	initAgent.logException(e.getMessage(), e);
        } catch (OntologyException e) {
        	initAgent.logException(e.getMessage(), e);
        }
    }
}
