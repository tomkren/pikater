package org.pikater.shared.experiment.webformat.server;

import java.util.HashMap;
import java.util.Set;

import org.pikater.shared.experiment.webformat.AbstractExperimentGraph;
import org.pikater.shared.experiment.webformat.client.ExperimentGraphClient;

public class ExperimentGraphServer extends AbstractExperimentGraph<BoxInfoServer>
{
	private static final long serialVersionUID = -2243894236748661673L;
	
	public ExperimentGraphClient toClientFormat()
	{
		ExperimentGraphClient result = new ExperimentGraphClient();
		for(BoxInfoServer box : leafBoxes.values())
		{
			result.addBox(box.toClientFormat());
		}
		result.edges = new HashMap<Integer, Set<Integer>>(edges); // simply copy, all IDs are kept intact
		return result;
	}
}