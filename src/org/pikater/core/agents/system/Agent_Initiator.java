package org.pikater.core.agents.system;

import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import org.pikater.core.agents.configuration.AgentConfiguration;
import org.pikater.core.agents.configuration.Argument;
import org.pikater.core.agents.configuration.Configuration;
import org.pikater.core.agents.configuration.ConfigurationProvider;
import org.pikater.core.agents.configuration.XmlConfigurationProvider;
import org.pikater.core.agents.PikaterAgent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class Agent_Initiator extends PikaterAgent {
	
	private static final long serialVersionUID = -3908734088006529947L;
	
	private String fileName = "core" +
			System.getProperty("file.separator") +
			"configurationMaster.xml";

	@Override
	protected void setup() {		
		initDefault();
		registerWithDF();
		
		System.out.println("Configuration: " + fileName);
		
		// read agents from configuration
		try {
			XmlConfigurationProvider configProvider = new XmlConfigurationProvider(fileName);

			Configuration configuration=configProvider.getConfiguration();
            List<AgentConfiguration> agentConfigurations=configuration.getAgentConfigurations();
            for (AgentConfiguration agentConfiguration : agentConfigurations)
            {
                //Preimplemented jade agents do not count with named arguments, convert to string if necessary
                Object[] arguments=ProcessArgs(agentConfiguration.getArguments().toArray());
                Boolean creationSuccessful=this.CreateAgent(agentConfiguration.getAgentType(),agentConfiguration.getAgentName(),arguments);
                if (!creationSuccessful)
                {
                    logError("Creation of agent "+agentConfiguration.getAgentName()+" failed.");
                }
            }
		}
        catch (Exception e) {
			e.printStackTrace();
		}
		
		addBehaviour(new TickerBehaviour(this, 60000) {
		  Calendar cal;
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  
		  protected void onTick() {
			  cal = Calendar.getInstance();
			  System.out.println(myAgent.getLocalName()+": tick="+getTickCount()+" time="+sdf.format(cal.getTime()));
		  }
		});
	}

	public Boolean CreateAgent(String type, String name, Object[] args) {
		// get a container controller for creating new agents
		PlatformController container = getContainerController();
		
		if (args.length > 0) System.out.println(type + " " + name + " " + args[0]);
		
		try {
			AgentController agent = container.createNewAgent(name, type, args);
			agent.start();
			// provide agent time to register with DF etc.
			doWait(300);
		} catch (ControllerException e) {
			System.err.println("Exception while adding agent: " + e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

    public  Object[] ProcessArgs(Object[] args)
    {
         Object[] toReturn=new Object[args.length];
         for (int i=0;i<args.length;i++)
         {
               Argument arg=(Argument)args[i];
              if (arg.getSendOnlyValue())
              {
                  toReturn[i]=arg.getValue();
              }
             else
              {
                  toReturn[i]=args[i];
              }
         }
        return  toReturn;
    }
    
    @Override
    public void initDefault()
    {
           Object[] args = getArguments();
            
           if (args != null && args.length > 0) {
        	   fileName = (String) args[0];
           }
           
           initLogging();
    }
}