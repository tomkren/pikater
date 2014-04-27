#Extended Pikater

This project extends the <a href="https://github.com/peskk3am/pikater4">Pikater</a> project with multi-user support, web based GUI and distributed infrastructure to allow simultaneous computation of many individual tasks. For that purpose, the following technologies are used:
* <a href="https://vaadin.com/home">Vaadin framework</a> based on Google Web Toolkit (GWT).
* <a href="https://github.com/ericdrowell/KineticJS">KineticJS</a> for experiment editor drawing (see main features) and <a href="https://github.com/neothemachine/KineticGWT">KineticGWT</a> for easy development in GWT.
* <a href="http://jade.tilab.com/">Jade</a> multi-agent system for easy integration with Pikater and the infrastructure it provides.

##Main features

Client:
* 2D editor to define experiments (boxes and edges style).
* Potentially many experiments may be scheduled to execution.
* Displaying the experiment results and converting/downloading them into a human-readable format, such as CSV.
* Uploading custom data sets, computation methods and models.

Server:
* Many features of the original Pikater project, such as computation method recommendation.
* Experiments planning and execution.
* Saving of trained models which can then be used in further experiments.
* Administrator functions, such as supervision of all scheduled experiments.


##How it works

See <a href="https://www.dropbox.com/s/uum4080mpk068h0/Architektura.png">this</a> picture.

##How to use

###Launching from a servlet container

1) Install and prepare a PostGRE database. Change the following files accordingly:
* src/beans.xml
* src/META-INF/persistence.xml

2) Edit the `WebContent/WEB-INF/conf` directory.

3) Install and prepare a servlet container (Apache Tomcat, …).

4) Deploy the "WebContent" into the servlet container and start it.

5) Access the page, enter the default login and password, follow the application launching wizard.

###Launching from a command line

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

##Documentation

###Core
* <a href="http://jade.tilab.com/doc/">Jade documentation</a>

###Shared
* <a href="http://docs.oracle.com/javaee/5/tutorial/doc/bnbpz.html">Java persistence API documentation</a>
* <a href="http://docs.oracle.com/cd/E16439_01/doc.1013/e13981/cmp30cfg.htm">Using Java Persistence API</a>
* <a href="http://epaul.github.io/jsch-documentation/javadoc/">JSch javadoc</a>

###Web server
* <a href="https://vaadin.com/book/-/page/preface.html">Book of Vaadin 7</a>
* <a href="http://tomcat.apache.org/tomcat-7.0-doc/servletapi/">Servlet 3.0 documentation</a>
* <a href="http://tomcat.apache.org/tomcat-7.0-doc/logging.html">Logging in Apache Tomcat</a>