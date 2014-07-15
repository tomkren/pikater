<!-- --- title: GitHub overview -->

This project extends the [Pikater](https://github.com/peskk3am/pikater4) project with multi-user support, web based GUI and distributed infrastructure to allow simultaneous computation of many individual tasks. For that purpose, the following principal technologies are used:
* [Jade](http://jade.tilab.com/) multi-agent system. Required by the original Pikater project.
* [Vaadin](https://vaadin.com/book/-/page/preface.html) framework based on [Google Web Toolkit (GWT)](http://www.gwtproject.org/) to build the aforementioned GUI.
* [Java persistence API (JPA)](http://en.wikipedia.org/wiki/Java_Persistence_API). This way, we can make sure the project could be rewritten to support various relational databases with minimum effort.
* [KineticJS](https://github.com/ericdrowell/KineticJS) to draw experiments on the client (see main features) and [KineticGWT](https://github.com/neothemachine/KineticGWT) for easy integration in GWT.




## Main features

Client:
* 2D editor to define experiments (boxes and edges style).
* Potentially many experiments may be scheduled to execution.
* Displaying the experiment results and converting/downloading them into a human-readable format, such as CSV.
* Uploading custom data sets and computation methods.

Server:
* Many features of the original Pikater project, such as computation method recommendation.
* Experiments planning and execution.
* Saving of trained models which can then be used in further experiments.
* Administrator functions, such as supervision of all scheduled experiments.




## How to use

### Deployment

1. Install and prepare the database (currently only PostGRE is supported). Change the following files accordingly:
	* `src/beans.xml`
	* `src/META-INF/persistence.xml`
2. Edit files in the `WebContent/WEB-INF/conf` directory:
	* `appServer.properties` is used to configure the web application.
	* `jadeTopology1.xml` is used to provide directives to additional machines - one master machine to coordinate the application's core and other slave machines to do the work.
3. Install and prepare a servlet container (e.g. Apache Tomcat), deploy the application into it and start it.

### Launching

#### From a servlet container

1. Access the page, e.g. go to `http://localhost:8080/Pikater` (for Apache Tomcat) or `http://my.domain/Pikater`. A login dialog will appear.
2. Enter the default login and password (from `WebContent/WEB-INF/conf/appServer.properties` - see above) and follow the launch wizard.

#### From the command line

<font color="red">// TODO: make this properly</font>

`-Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl
-Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl`

Tj. např.:

`java -Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl -cp jade.jar jade.Boot -local-host localhost -gui -agents mailAgent:pikater.agents.utility.MailAgent;mailTester:org.pikater.core.agents.system.mailing.MailAgentTester`

Pro spuštění InitiatorAgenta co spustí výpočet:  
Nejlepší použít soubory `/core/runPikaterMaster.sh` a `/core/runPikaterSlave.sh`

Centralni bod:
`java -Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl -cp jade.jar jade.Boot -local-host localhost -gui -local-host localhost -agents initiator:org.pikater.core.agents.system.Agent_Initiator(core/configurationMaster.xml)`

Jeden vypocetni server:
`java -Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl -Djavax.xml.parsers.SAXParserFactory=com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl -cp jade.jar jade.Boot -local-host localhost -gui -local-host localhost -agents initiator:org.pikater.core.agents.system.Agent_Initiator(core/configurationSlave.xml)`




## Information about this repository

<font color="red">// TODO: information/description/commentaries to individual files/folders</font>




## Documentation

* <a href="docs/Documentation.md">Open documentation overview</a>

