#!/bin/sh

if [ $# -ne 1 ]; then
  echo "usage: $0 <name-of-slave-node>" >&2
  exit 1
fi

cd "`dirname $0`/.."

java -Djavax.xml.parsers.DocumentBuilderFactory="com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl" -Djavax.xml.parsers.SAXParserFactory="com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl" -cp libraries/*:libraries/aa/*:libraries/beans/*:libraries/commons-logging-1.1.3/*:libraries/java-json/*:libraries/jpaEclipseLink/*:libraries/junit/*:libraries/reflections/*:libraries/spring-framework/*:libraries/spring-framework-4.0.0.M2/libs/*:libraries/xstream-1.4.7/lib/*:libraries/xstream-1.4.7/lib/xstream/*:libraries/xstream-1.4.7/lib/xstream-hibernate/*:bin jade.Boot -jade_core_management_AgentManagementService_agentspath core/ext_agents -container "initiator-$1:org.pikater.core.agents.system.Agent_Initiator(core/configurationSlave.xml,$1)"
