package pikater.utility.pikaterDatabase.tables.experiments;

import java.util.ArrayList;

import pikater.utility.pikaterDatabase.tables.experiments.parameters.UniversalParameter;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalConsumerSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalProviderSlot;


public class UniversalComputingDescription {

	private ArrayList<UniversalDataFlow> elements = new ArrayList<UniversalDataFlow>();
    private ArrayList<UniversalParameter> globalParameters = new ArrayList<UniversalParameter>();

    private boolean hadGraphicalInput = false;


    public void addDataFlow(UniversalDataFlow element) {
    	this.elements.add(element);
    }
    public void addGlobalParameter(UniversalParameter parameter) {	
    	this.globalParameters.add(parameter);
    }
    
    public ArrayList<UniversalDataConsumer> getConsumers() {
    	
    	ArrayList<UniversalDataConsumer> consumers = new ArrayList<UniversalDataConsumer>();
    	
    	for (UniversalDataFlow d : this.elements) {
    		if (d instanceof UniversalDataConsumer) {
    			consumers.add((UniversalDataConsumer) d);
    		}
    	}
    	
    	return consumers;
    }


    public ArrayList<UniversalDataFlow> getElements() {
    	return this.elements;
    }
    public void setElements(ArrayList<UniversalDataFlow> elements) {
    	this.elements = elements;
    }

    public ArrayList<UniversalParameter> getGlobalParameters() {
    	return this.globalParameters;
    }
    public void setGlobalParameters(ArrayList<UniversalParameter> globalParameters) {
    	this.globalParameters = globalParameters;
    }

    public void setHadGraphicalInput(boolean hadGraphicalInput) {
    }
    public boolean getHadGraphicalInput() {
    	return this.hadGraphicalInput;
    }


    public boolean connect(UniversalProviderSlot providerSlot, UniversalConsumerSlot consumerSlot) {
    	return consumerSlot.connect(providerSlot);
    }

    public boolean disConnect(UniversalProviderSlot providerSlot, UniversalConsumerSlot consumerSlot) {
    	return consumerSlot.disCconnect(providerSlot);
    }


/*
    public UniversalComputingDescription importOntology(ComputationDescription comDesc) {
    	
    	//this.globalParameters = comDesc.getGlobalParameters();

    	for ( IVisualizer vis : vizcomDesc.getRootElements() ) {
    	}

    	
    	return null;
    }

    public static UniversalComputingDescription importGraph(GraphJSON graph) {
    	return null;
    }
*/
/*
    public GraphJSON exortGraph() {
    	return null;
    }
*/


    public static UniversalComputingDescription importXML(String xml) {
    	return new UniversalComputingDescription();
    }
    public String exportXML() {
    	return "Exported XML :-)";
    }

}
