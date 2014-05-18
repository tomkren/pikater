package org.pikater.core.agents.experiment.virtual;


import jade.content.onto.Ontology;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.ontology.actions.AgentInfoOntology;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.CARecSearchComplex_VirtualBox;
import org.pikater.core.options.FileInput_VirtualBox;
import org.pikater.core.options.FileSaver_VirtualBox;

public class Agent_VirtualBoxProvider extends Agent_AbstractExperiment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7569621739765373832L;

    @Override
	public java.util.List<Ontology> getOntologies() {
		
		java.util.List<Ontology> ontologies =
				new java.util.ArrayList<Ontology>();
		ontologies.add(AgentInfoOntology.getInstance());
		
		return ontologies;
	}
 
    @Override
    protected void setup() {
    	
        initDefault();
        
        registerWithDF(AgentNames.VIRTUAL_BOX_PROVIDER);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		AgentInfo fileInput = FileInput_VirtualBox.get(); 
		sendAgentInfo(fileInput);

		AgentInfo fileSaver = FileSaver_VirtualBox.get(); 
		sendAgentInfo(fileSaver);
		
		AgentInfo cARecSearchComplex = CARecSearchComplex_VirtualBox.get(); 
		sendAgentInfo(cARecSearchComplex);

    }  // end setup()
    
    
 	@Override
 	protected AgentInfo getAgentInfo() {
 		// This agent doesn't correspond to any real experimental agent
 		return null;
 	}

}
