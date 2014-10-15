package org.pikater.core.agents.system.manager;

/**
 * User: Kuba
 * Date: 7.11.13
 * Time: 14:16
 */
public class AgentTypeDefinition {
	
    String name;
    String typeName;

    /**
     * Constructor
     * 
     * @param typeName - agent class
     */
    public AgentTypeDefinition(String name, String typeName) {
        this.name = name;
        this.typeName = typeName;
    }

    /**
     * Get agent name
     */
    public String getName() {
        return name;
    }

    /**
     * Set agent name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get agent type
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Set agent type
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
