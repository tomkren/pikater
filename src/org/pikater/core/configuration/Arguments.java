package org.pikater.core.configuration;

import jade.util.leap.Serializable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Kuba Date: 9.11.13 Time: 17:27
 */
public class Arguments implements Serializable {
	private static final long serialVersionUID = -6400109657193599321L;

	public Arguments(Map<String, Argument> arguments) {
		this.arguments = arguments;
	}

	public Arguments() {
		arguments = new HashMap<String, Argument>();
	}

	protected Map<String, Argument> arguments;

	public Collection<Argument> getArguments() {
		return arguments.values();
	}

	public String getArgumentValue(String argName) {
		return arguments.get(argName).getValue();
	}

	public Boolean containsArgument(String argName) {
		return arguments.containsKey(argName);
	}

	public Boolean isArgumentValueTrue(String argName) {
		if (!containsArgument(argName)) {
			return false;
		}
		String argValue = getArgumentValue(argName);
		return argValue.equals("1") || argValue.equalsIgnoreCase("true");
	}

	public int size() {
		return arguments.size();
	}
}
