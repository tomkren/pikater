#!/bin/sh


java -Djavax.xml.parsers.DocumentBuilderFactory="com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl" -Djavax.xml.parsers.SAXParserFactory="com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl" -cp libraries/*:libraries/commons-logging-1.1.3/*:libraries/spring-framework-4.0.0.M2/libs/*:bin jade.Boot -gui initiator:pikater.agents.management.InitiatorAgent
