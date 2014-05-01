#!/bin/sh

cd ..;

java -Djavax.xml.parsers.DocumentBuilderFactory="com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl" -Djavax.xml.parsers.SAXParserFactory="com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl" -cp libraries/*:libraries/aa/*:libraries/beans/*:libraries/commons-logging-1.1.3/*:libraries/java-json/*:libraries/jpaEclipseLink/*:libraries/junit/*:libraries/reflections/*:libraries/spring-framework/*:libraries/spring-framework-4.0.0.M2/libs/*:libraries/xstream-1.4.7/lib/*:libraries/xstream-1.4.7/lib/xstream/*:libraries/xstream-1.4.7/lib/xstream-hibernate/*:bin jade.Boot -gui 'initiator:org.pikater.core.agents.system.Agent_Initiator(core/configurationMaster.xml)'
