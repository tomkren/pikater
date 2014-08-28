<!-- --- title: GitHub overview -->

This project extends the [Pikater](https://github.com/peskk3am/pikater4) project with multi-user support, web based GUI and distributed infrastructure to allow simultaneous computation of many individual tasks.

As such, this application consists of 3 main parts:

1. Core system  
The original pikater project. Although it has been rewritten to support the extension, it still works as a standalone unit.
2. GUI extension
3. Inline documentation written in markdown, easily available as a wiki.

More information on each of these "components" can be found in the documentation.




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




## Life-cycle

### Installation

First and foremost, note several things:

1. This repository has several branches. Two of them are important:
	* `master` - contains the latest core system version.
	* `Eclipse-Vaadin-project` - contains the latest GUI and documentation versions.
2. This project requires a JPA-compatible database. At this moment, only PostGRE database is supported.

#### Database

1. Install the database.
2. When you clone this project on your machines in later steps, change the following files accordingly:
	* `src/beans.xml`
	* `src/META-INF/persistence.xml` (<font color="red">TODO</font>)

#### Core system

Clone the `master` branch on the target machine.

#### Extension

Import the project in Eclipse:

1. Install [Eclipse](https://www.eclipse.org/downloads/).
2. Install the [Vaadin plugin for Eclipse](http://vaadin.com/eclipse).
3. Clone the `Eclipse-Vaadin-project` branch on the target machine.
4. Compile the `org.pikater.web.vaadin.gui.PikaterWidgetset.gwt.xml` widgetset.
5. Inspect/edit files in the `WebContent/WEB-INF/conf` directory:
	* `appServer.properties` is used to configure the web application.  
6. Install and prepare a servlet container (e.g. Apache Tomcat).

### Deployment & launching

#### Database

Make it publicly available to the machines you wish to run core system or extension on.

#### Core system

Deployment was already done in installation.  
To launch the core system, execute the `Launch-core.launch` configuration.

#### Extension

At this moment, the extension requires an instance of core system running locally so install and launch it first.

To deploy and launch the extension:

1. Start the servlet container.
2. Export the extension into a `.war` file from Eclipse, deploy it into the servlet container.
3. Start the deployed application.

<font color="red">TODO</font>

4. Access the page, e.g. go to `http://localhost:8080/Pikater` (for Apache Tomcat) or `http://my.domain/Pikater`. A login dialog will appear.
5. Enter the default login and password (from `WebContent/WEB-INF/conf/appServer.properties` - see above) and follow the launch wizard.

### Maintenance

If extension is used, one or more administrators may need to manage it using the GUI. Otherwise, the application should be self-maintained.




## Wiki

This project contains inline documentation written in markdown, easily available as a wiki.

### Installation

1. Clone the `Eclipse-Vaadin-project` branch of [pikater's GitHub repository](https://github.com/krajj7/pikater).
2. [[How to install|docs/guide_admin/wiki/01-Installation]]

### Launching

[[How to launch|docs/guide_admin/wiki/02-Launching]]

### Usage

[[How to use|docs/guide_admin/wiki/03-Usage]]




## Documentation

[[Open documentation overview|Overview]]
