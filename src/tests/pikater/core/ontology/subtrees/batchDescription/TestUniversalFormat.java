package tests.pikater.core.ontology.subtrees.batchDescription;

import org.pikater.core.agents.gateway.WebToCoreEntryPoint;
import org.pikater.core.agents.gateway.exception.PikaterGatewayException;

public class TestUniversalFormat {

	public static void main(String [ ] args) {

		try {
			WebToCoreEntryPoint.notify_newBatch(119554, 5858); //(103601);
		} catch (PikaterGatewayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
