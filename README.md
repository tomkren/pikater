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
* A SMTP server running locally.

### Installation & deployment

1. Install and prepare the database (currently only PostgreSQL is supported).
2. Clone the project.
3. Change the following files accordingly:
	* `src/beans.xml`
	* `src/META-INF/persistence.xml`
4. Resolve Ivy dependencies.
5. Run `org.pikater.shared.database.util.DatabaseInitialisation.java`.

### Launching

Run the `Pikater-core.launch` launch configuration.

## <font color="red">TODO</font>

1. Offer to bundle the project, including all dependencies. Best use maven since it's available as a command line tool on many platforms. This effectively gets rid of the "build*.xml" files which need to be maintained manually.
2. Provide information about this repository.
3. Documentation - move related things from Vaadin extension in here.
4. Rewrite "tests" into proper JUnit tests and integrate them in suites.
5. Get rid of all the "core" folder insanity. Clean and organize it.