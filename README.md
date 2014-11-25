This project revises the original [Pikater](https://github.com/peskk3am/pikater4) project:

1. Previous database access was rewritten into a DBMS independent (almost) framework using JPA.
2. Logging has been rewritten and centralized.
3. Ivy dependency management was added to tame dependencies and to allow their easy integration in other projects building upon this one.
4. Allows external manipulation via the JPA framework mentioned above and the following "RPC services":  
`org.pikater.core.agents.gateway.WebToCoreEntryPoint.java`

## How to use

### Requirements

* Java 7
* Apache Ivy.
* JPA-compatible database (at this moment, only PostgreSQL is supported) running locally or externally.
* A SMTP server running locally.

### Installation & deployment

1. Install PostgreSQL database. Use a package manager or download [here](http://www.postgresql.org/download/). Pikater was developed with version 9.3 and we highly recommended using it.
2. Create a new database in PostgreSQL. Using UTF-8 character encoding is recommended.
3. Clone the project.
4. Change the following files accordingly:
	* `src/beans.xml`
	* `src/META-INF/persistence.xml`
5. Resolve Ivy dependencies.
6. Run `org.pikater.shared.database.util.DatabaseInitialisation.java` to generate configuration files and your first Pikater administrator account.

### Launching

Run the `Pikater-core.launch` launch configuration.

## Other extending projects

* [Vaadin GUI frontend](https://github.com/SkyCrawl/pikater-vaadin)

## <font color="red">TODO</font>

1. Offer to bundle the project, including all dependencies. Best use maven since it's available as a command line tool on many platforms. This effectively gets rid of the "build*.xml" files which need to be maintained manually.
2. Provide information about this repository.
3. Documentation - move related things from Vaadin extension in here.
4. Rewrite "tests" into proper JUnit tests and integrate them in suites.
5. Get rid of all the "core" folder insanity. Clean and organize it.