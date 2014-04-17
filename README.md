pikater (fork)
=======

pikantni agentni teritorium (project in jade)
dočasný fork


### Parametry JVM ke spuštění:

`-Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl
-Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl`

Tj. např.:

`java -Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl -cp jade.jar jade.Boot -local-host localhost -gui -agents mailAgent:pikater.agents.utility.MailAgent;mailTester:org.pikater.core.agents.system.mailing.MailAgentTester`

Pro spuštění InitiatorAgenta co spustí výpočet:
Nejlepis pouzit soubory /core/runPikaterMaster.sh /core/runPikaterSlave.sh

Centralni bod:
`java -Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl -cp jade.jar jade.Boot -local-host localhost -gui -local-host localhost -agents initiator:org.pikater.core.agents.system.Agent_Initiator(core/configurationMaster.xml)`

Jeden vypocetni server:
`java -Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl -cp jade.jar jade.Boot -local-host localhost -gui -local-host localhost -agents initiator:org.pikater.core.agents.system.Agent_Initiator(core/configurationSlave.xml)`
