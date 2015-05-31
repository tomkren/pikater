# Jak to rychle rozchodit v IntelliJ

1. mějte IntelliJ IDEA (testováno na verzi 14.1.3, de studentská licence zadarmo pro ulitamte version) (Java 8)
2. check out from version control (možnost při čerstvym installu) -> github 
    * nebo file -> new -> project from version control -> GitHub.
    * přihlásit atd 
    * https://github.com/tomkren/pikater.git
3. Instalace pluginu IvyIDEA pro stažení dependencies
    * file -> settings -> plugins
    * vyhledat ivy, napíše nothing to show
    * click na browse 
    * IvyIDEA (to s nejvíc staženíma)
    * install plugin (zelený tlačítko na pravo)
    * a budechtít restartovat, tak restarovat
4. file -> project structure
    * Project SDK: New.. -> JDK
    * najít cestu k něco jako C:\Program Files\Java\jdk1.8.0_25 
5. run -> edit configurations
    * +átko -> application
    * pojmenovat..
    * main class: jade.Boot
    * program arguments: -gui -agents "mama:org.pikater.core.agents.system.Agent_Initiator"






# Old README

This project revises the original [Pikater](https://github.com/peskk3am/pikater4) project:

1. Previous database access was rewritten into a DBMS independent (almost) framework using JPA.
2. Logging has been rewritten and centralized.
3. Ivy dependency management was added to tame dependencies and to allow their easy integration in other projects building upon this one.
4. Allows external manipulation via the JPA framework mentioned above and the following "RPC services":  
`org.pikater.core.agents.gateway.WebToCoreEntryPoint.java`

## How to use

### Requirements

* Java 7 (Java 8 may be supported too but noone has tested it yet).
* Apache Ant command line utility.
* A JPA-compatible database (at this moment, only PostgreSQL is supported) running locally or externally.
* A SMTP server running locally.

### Installation & deployment

1. Install PostgreSQL database. Use a package manager or download [here](http://www.postgresql.org/download/). Pikater was developed with version 9.3 and we highly recommended using it.
2. Create a new database in PostgreSQL. Using UTF-8 character encoding is recommended.
3. Install Apache Ant command line utility.
4. Clone the project.
5. Change the following files accordingly:
	* `src/beans.xml`
	* `src/META-INF/persistence.xml`
6. Go to the cloned repository and execute: `ant -f pikater-core.xml -lib lib/ivy-2.4.0.jar build`.
7. <font color="red">TODO:</font> Run `org.pikater.shared.database.util.DatabaseInitialisation.java` to generate configuration files and your first Pikater administrator account.
8. <font color="red">TODO:</font> launch

### Launching



## Other extending projects

* [Vaadin GUI frontend](https://github.com/SkyCrawl/pikater-vaadin)

## Database schema

It can be generated (along with other useful information) by the `db-schema-gen.sh` script.

Our compliments to [SchemaSpy](http://schemaspy.sourceforge.net/) to make this happen. SchemaSpy's `.jar` distribution file can be located in the `lib` folder.

## <font color="red">TODO</font>

1. Provide information about this repository.
2. Documentation - move related things from Vaadin extension in here.
3. Rewrite "tests" into proper JUnit tests and integrate them in suites.
4. Get rid of all the "core" folder insanity. Clean and organize it.