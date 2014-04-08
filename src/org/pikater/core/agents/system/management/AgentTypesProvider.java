package org.pikater.core.agents.system.management;

import java.util.Map;

/**
 * User: Kuba
 * Date: 31.10.13
 * Time: 16:49
 */
public interface AgentTypesProvider {
    Map<String,AgentTypeDefinition> GetAgentTypes();
}
