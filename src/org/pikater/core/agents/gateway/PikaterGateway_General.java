package org.pikater.core.agents.gateway;

import org.pikater.core.agents.gateway.exception.PikaterGatewayException;

import jade.core.Profile;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;

public class PikaterGateway_General {
	
	public static int MASTER_PORT = 1099;
	
	/**
	 * Constructor
	 */
	private PikaterGateway_General() {
	}
	
	public static void initJadeGateway(){
		PikaterGateway_General.initJadeGateway(MASTER_PORT);
	}
	
	public static void initJadeGateway(int port) {
		
		Class<Agent_PikaterGateway> gateway = Agent_PikaterGateway.class;
		Properties pp = new Properties();
		pp.setProperty(Profile.MAIN, "false");
		pp.setProperty(Profile.MAIN_PORT, ""+port);
		JadeGateway.init(gateway.getName(), pp); 
	}
	
	public static void generalRequest(Initiator initiator
			) throws PikaterGatewayException{
		PikaterGateway_General.generalRequest(initiator, MASTER_PORT);
	}
	
	public static void generalRequest(Initiator initiator, int port
			) throws PikaterGatewayException{
		
		try {
			
		JadeGateway.execute(initiator);

		JadeGateway.shutdown();
		} catch (ControllerException e) {
			PikaterGatewayException x =
					new PikaterGatewayException("JadeGateway.execute() failed");
			x.setStackTrace(e.getStackTrace());
			throw x;
		} catch (InterruptedException e) {
			PikaterGatewayException x =
					new PikaterGatewayException("JadeGateway.execute() failed");
			x.setStackTrace(e.getStackTrace());
			throw x;
		}
		
	}
}
