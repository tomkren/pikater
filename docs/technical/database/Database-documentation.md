<!-- --- title: Technical Documentation - Part I: Documentation of Database -->

[[_TOC_]]

## Preface

Pikater's main task is executing some experiments using input data and several types of output data are created as result, such as new classified data set and statistics of computation in case of a classification task. These data should be saved somewhere and made available for upcoming experiments.  

### Used Acronyms

* __ARFF__ (Attribute-Relation File Format)  
is a widely-used format for input files of machine learning experiment, that is also used in Pikater 
* __CSV__ Comma-Separated Values)
* __JDBC__ (Java Database Connectivity)
* __SQL__ (Strucutered Query Language)
* __HSQLDB__ (Hyper SQL Database)
* __PgLOB__ (Postgre Large Object)
* [__JPA__](http://docs.oracle.com/javaee/6/tutorial/doc/bnbpz.html) (Java Persistence API)
* __DAO__ - Data Access Object
* __ACID__ - Atomicity, Consistency, Isolation, Durability
* __JPQL__ - Java Persistence Query Language


### Used Terms

* __Dataset__  
Alias for ARFF files, that are used as input data for experiments.
* __Entity__
Java class object, that is prepared to work with Java Persistence API




## Choice of Database Management System

Version of Pikater before our project team has started working on it used to use MySQL as the main storage of created data. Access to the the DBMS run through MySQL's JDBC driver and querying was tackled by dynamic generation of SQL queries. Results of these queries were classic ResultSet objects implemented in Java's database framework.
The main reason we decided to change this approach was its bound to the specific version of DBMS. Moreover, we could face incompatibility of current queries when we wanted to change the database schema.
Changes in database schema are rare and should be less common in production environment - it's really bad practice to do major changes in a system, that is actively being used, but Pikater's production environment will be more like experimental - however, changes of schema in the old version could lead to inconsistency between the queries and the database, that should have been tackled by massive debugging and code rewriting. 

**PostgreSQL** 
We considered three database management systems before we decided for our final choice: MySQL, PostgreSQL and HyperSQL DB (HSQLDB). All of these DBMS have some versions that are available free of charge.  
The chosen DBMS must fulfilled several criteria, as follows:
- support for multiple platforms
- support for transactional queries
- support for larger files (cca. hundreds of MiBs)
The last criteria is result of our analysis, when we searched for the best way, how can we optimally store the input data files for experiments.
The following table shows summary about important properties of considered DBMSs:

|               |   MySQL         |   HSQLDB         | PostgreSQL           |
|:-------------:|:---------------:|:----------------:|:--------------------:|
| transactions  | yes (InnoDB)    | yes              | yes                  |
| files (LOBs)  | yes             | yes (64TiB for all)| yes (4TiB for each file)|
| other         | used in original project, widespread | Java  | widespread usage     |

Despite the fact that MySQL supports transactional queries, we can't use this feature, because transactional queries require using InnoDB engine, that is not available in the free version of MySQL.

Choice between HSQLDB and PostgreSQL was mainly based on their approach to binary objects. Current DBMSs support storing byte array in the fields of database records. These byte arrays are ideal place to store files. The only problem - in the way of effectiveness rather than availability - can cause the access to these byte arrays.
PostgreSQL uses its own way to access particular byte arrays, that supports copying from and to the database with streams. Using HSQLDB we have to use setters and getters to access these records, that work with standard Java class java.lang.Object. Considering the fact, that we are planning operations with larger files, we made decision in favour of PostgreSQL.
Moreover, from a bit subjective point of view, PostgreSQL is more widely used, that theoretically can mean better availability of information for it and thus more simple solution of problems.




## Transition to Java Persistence API

As we mentioned before, the old version of Pikater used access to database, that was strictly bound to that particular version of DBMS. We decided, that this access would be changed in a way, that it would use Java Persistence API (JPA). JPA allows mapping database records to objects and vice versa.

The main advantage of JPA is, that objects prepared to work with JPA don't have to be changed if the underlying DBMS is changed. These prepared objects are called entities and they are recognisable by tag `@Entity` before the java class definition.

### Choice of JPA Connector

Decision for JPA meant, that we not just needed a database and technology to access it, but also something that connects the world of Java with the world of database. Practically, that means translation of operations in JPA to SQL queries with the same semantics. In opposite way, this is translation of results to queries to entity objects. All of these translations are tackled by some JPA connector. Choice of this connector doesn't have direct influence to the application development, except to some special features. Two most widespread JPA connectors are Hibernate and Eclipselink. We decided for Eclipselink, because it supports changing properties of the database connection from source code without using external configuration files, that can be useful in the future.

### Configuration

Old version of Pikater used only file `Beans.xml` for configuration of database connection and using library Spring the object of database connection was injected into the application. This object was retrieved using function `ApplicationContext.getBean`.

Pikater still needs native access to the database, mainly because Postgre's Large Objects are used, and this is the reason we decided to preserve file `Beans.xml`. For less painful transition we left it in original location in root folder of the source code.

Part of file `Beans.xml` that contains data necessary to establish a connection to the database: 
```xml
...
<bean
id="defaultConnection"
class="org.pikater.shared.database.connection.PostgreSQLConnectionProvider"
scope="singleton">
        <constructor-arg index="0">
                  <value><!-- url --></value>
        </constructor-arg>
        <constructor-arg index="1">
                  <value><!-- database username --></value>
        </constructor-arg>
        <constructor-arg index="2">
                  <value><!-- password for database user--></value>
        </constructor-arg>
</bean>
...
```

Except for the native access, we also needed connection to the database for the JPA entity manager. Morever, it needs information about Java classes, that should be used as entities. These informations are located in the configuration file `persistence.xml`, that must be located in folder `META-INF`. This file must contain list of entities in the form as the following example shows:
```xml
<persistence-unit name="pikaterDataModel" transaction-type="RESOURCE_LOCAL">
<provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
…
<class>org.pikater.shared.database.jpa.JPADataSetLO</class>
...
```
Definition of database connection can be tackled in several ways. First of all, we can add the needed information to the file `persistence.xml`. Another way is using library Spring for injecting the entity manager (class `EntityManagerFactory`), that have own way to configure the connection.

We decided to only use the `persistence.xml` file for the following reasons:

* when creating the EntityManagerFactory, we can set connection properties from the source code
* we have more freedom at implementation of classes, that manage the entities 

It was a challenge for us to create the `EntityManagerFactory` using `Beans.xml`, because of Spring's internal usage of `Proxy` classes when creating instances of beans. This means, that for every interface implemented by particular bean, new `Proxy` object is created, which must be casted to the type of interface. `EntityManagerFactory` has many interfaces implemented and naturally none of them covers the functionality of the whole class and that is the reason, why approach using `ApplicationContext.getBean` function doesn't work. This was also the reason, why we didn't want inject objects of `EntityManager` class (retireved from `EntityManagerFactory`) to objects manipulating with entities. If these objects were injected using Spring, we would need interface covering whole functionality for each class.

For initial creation of configuration files can be used program `org.pikater.shared.database.util.initialisation.DatabaseInitialisation`, that can be used from command line and it creates all configuration files.


## Database

### Database Schema

#### Original DB Schema

The following picture shows visualisation of the old database schema used in original version of Pikater. 

[[original_db_schema.png|alt=Original DB schema]]

#### Actual DB Schema

If we use Java Persistence API to access the database, it automatically creates database tables that reflect the entities. The following picture shows structure of database for entities used in Pikater.  

[[schemaspy_output/diagrams/summary/relationships.real.large.png|alt=Current DB schema]]

#### Differences between Original and Actual Schema

When we created the structure of JPA entities, we wanted that the final table structure is similar to the original one. On the other hand, some changes were inevitable to reflect relations between entities (e.g. experiment can have several subexperiments) and create basic elements for new functions (e.g. saving of datasets requires at least records about OIDs).

|               | Original Pikater |  Actual Pikater  |
|:-------------:|:----------------:|:----------------:|
| datasets      | files on disk    | database         |
| other data    | database         | database         |

### PostgreSQL Specific Features

#### Postgre Large Objects

After the decision that PosgtreSQL would be the DBMS we use in Pikater, we had to implement the access to the files stored in database.

Files are stored using PostgreSQL Large Objects (PgLOBs or simply LOBs). These LOBs are saves as records in the `pg_largeobject` system table of particular database. For each file several records of byte arrays are created, where parts of files are copied. We need the ID of the PgLOB - which is called OID in Postgre's nomenclature, that is abbreviation for Object ID - to retrieve or store the content of file. As an example I can mention entity `JPADatasetLO`, where we have one variable dedicated for OID.

Despite the fact, that we can use annotation `@Lob` in JPA entities, that usually maps variable to byte array in database record, using it for large files is not recommended. In the worst case, it can happen, that the whole file is copied to memory before it's stored in database.

Classes working with PostgreSQL Large Objects can be found in package `org.pikater.shared.database.postgre.largeobject`.

### Using Database Framework 

Database Framework is implemented using classes, that are located in package `org.pikater.shared.database`. We tried to make subpackages represent their functionality straight-forward, but at least package `org.pikater.shared.database.jpa` deserves more detailed description. This package contains implementation of **entities** and **Data Access Objects**, that basically represent the core of database access. 

#### Entities

Entities have class names with prefix "JPA" (e.g. `JPAUseer`). In the following points we give a brief description of these classes. Please note, that entities are ordinary Java classes, that are prepared to be treated as JPA entities.

Currently we use 16 entities in Pikater, that are located in package `org.pikater.shared.database.jpa`. Each entity is inherited for abstract entity `JPAAbstractEntity`. This common ancestor have only one variable for ID, which is used as primary key in the database. 

##### Entities for Users

Users are mainly created using the web interface and these entities represent a crucial part of system. 

1. **JPAUser**
Entity of a user. Each entity having some owner is referencing an instance of this entity. It contains user's login name, their password in hashed format, e-mail address for notification mails, user's maximal priority, date and time information about creation and last login. Each user is active or suspended.  

2. **JPAUserPriviledge**  
Entity representing privileges available in system. Despite having privileges hard-coded in enum `org.pikater.shared.database.jpa.PikaterPriviledge`, we decided for also creating JPA entities, because it can be useful to have privileges stored in the database. 

3. **JPARole**  
Different subsets of privileges can be attached to some role, that is connected to user. 

##### Entities for Datasets

1. **JPADataSetLO**
`JPADataSetLO` entity contains data fields about datasets in the database. The most important field is OID, which is the ID of PgLOB, where the dataset is stored. Field hash is the MD5 hash, that is computed when one dataset is being uploaded. There can exist multiple `JPADataSetLO` objects for a given hash, but these datasets are considered equal during the upload process, hence they are only once stored in the database and entities have same OIDs.
Dataset's metadata are important for machine learning methods, because with them help one can determine similarity of two datasets without retrieving datasets' contents. That's the reason why these metadata are computed, when datasets are uploaded and they are saved to their corresponding entities, which are bound to the particular `JPADataSetLO` entity.

2. **JPAGlobalMetaData**  
Every dataset has these metadata, that contain information about how many entries (data rows) and which kind of attributes has the dataset.

3. **JPAAttributeMetadata**  
Relations (columns) of datasets have got their own metadata, that can be retrieved for every attribute e.g. ratio of missing values, class of entropy, whether an attribute is target or not and attribute's original order in dataset.

4. **JPAAttributeCategoricalMetadata**  
Some attributes can be defined as categorical - which contain values from a set, that is defined in the header - and their `JPAAttributeCategoricalMetadata` entity in addition to fields in `JPAAttributeMetadata` contains field for number of categories (simply the cardinality of the set defined in header). 

5. **JPAAttributeNumericalMetadata**  
This entity is similar to `JPAAttributeCategoricalMetadata` and is inherited from  `JPAAttributeMetadata`, too. It contains fields to describe numerical relations, that can contain values with floating point. When creating metadata, Weka computes - among others - interval, average, deviation of the values in the relation.

6. **JPATaskType**  
Each dataset has a default experiment. We presumed, that the list of default experiments will grow in future, thus we decided for using an entity describing these experiments. When new dataset has yet unknown default action, we can create a new entity for it.

##### Entities for Experiments

Pikater is created for executing experiments of machine learning. These experiments are created using web interface and passed to Pikater's core for further processing.

1. **JPABatch**  
When the user creates a new experiment using web interface an XML document is created, which describes the experiment and can be processed by the core. This document is stored in one field of this entity. Furthermore, several fields are used for monitoring the experiment's execution state, owner and timestamps of creation, execution and finish.
 
2. **JPAExperiment** 
Each experiment is split into smaller parts when being executed. We called these smaller parts experiments and original experiments received the name batch, hence the class names `JPABatch` and `JPAExperiment`. These subexperiments are executed independently, so we needed another entity to store timestamps and execution statuses. Moreover, each subexperiment has an attached list of `JPAResult` entities about how well performed the subexperiment on that particular agent. Usually, subexperiments have only one attached `JPAResult` entity.

3. **JPAModel**  
This entity is used to store subexperiment's trained model. Each model is identified by the name of agent, that is stored in the model. Models also can be marked for permanent storage, because old models are usually deleted from the database.

4. **JPAResult**
Statistics about some subexperiment is stored using this entity. Its fields contain the agent's name, several types of statistics (e.g. kappa statistics, different errors), settings of subexperiment, start and finish timestamps (subexperiments can have multiple results attached, that's why we need timestamps again).

5. **JPAExternalAgent**
User can add own agents to Pikater and to store this agent we needed this entity. Agent is stored in a byte array of the entity which is one of several fields of it. Other fields contain the description, owner, class name and the flag whether the agent is permitted for execution.

6. **JPAAgentInfo**
For each agent available in Pikater exists one instance of this entity. The main reason of this entity's existence is data transfer about available agents for web interface, that retrieves data from database rather than from core via the gateway.

##### General Entities for Pikater's Core

1. **JPAFilemapping**  
This entity represents pair of dataset's original filename and its MD5 hash value. The main reason, why this entity exists, is the need to translate filenames to hash values, so we can preserve from Pikater's original version the way, how datasets can be referenced in experiment files. New experiments could be added from command line by loading their XML file, which contained file names rather than some hash values. 

#### Data Access Objects

Data Access Objects (DAO) have class names with suffix "DAO". During runtime we can access instances of these classes with class `org.pikater.shared.database.jpa.daos.DAOs`.
DAOs are used to retrieve, store, update and delete entities. As an example can serve the followin code snippet:

```java:/src/org/pikater/shared/database/jpa/Showcase.java```

The main reason we have chosen this approach is the way how JPA works with entities. We can treat entities as class Java objects, which means that we can call their functions, change their variables. The problem is, after we stored entities in database - or using JPA terminology, we persisted them - changes are reflected to the entity's record. The only criterion is, that entity should still be in persistence context. On the other hand, it's a good practice to have this persistence context as small as possible.

Creation of specialised classes - also called as Data Access Objects - is one solution to tackle the problem of persistence context. These classes have functions, that implements actions available for certain entities. In most cases each entity have one corresponding DAO, but in some cases function in one DAO performs changes on several entity types. Naturally, JPA supports transactional oparetions, so these changes doesn't violate ACID constraints. Finally all entity-related operations are done through these objects and we don't have to care about persistence context. 

#### Querying

Effective searching among the set of record upon some condition is the main reason we decided to use some DBMS for data storage in Pikater. Data Access Objects have functions, that support - among basic operations - performing some complex queries. These complex queries are inspired by needs of Pikater itself, so unfortunately it should not be considered as a generic API for Pikater's database, but it can be an example, how future queries should be tackled. 

##### JPQL Queries

JPQL has got syntax very similar to SQL. We can dynamically create such queries during the runtime by creating its String representative. This approach is very flexible, but can be more confusing than other querying options, that are provided by JPA. Finally, similar technique was used in the original version of Pikater, that we wanted replace.   

##### Named Queries

Named queries are prepared queries written in JPQL language, which are stored in entity's source code or in an external configuration file. One can say, that this is rather rigid way how querying is done, but named queries can contain parameters, which can be replaced by some value during the runtime.

Large portion of Pikater's queries is tackled using named queries, that can always be found in entities marked with tags `@NamedQueries` and `@NamedQuery`.

The following code snippet can serve as an example how to use named queries. This query returns all `JPADataSetLO` objects that have been stored by user `:owner` :
```java
@NamedQuery(name="DataSetLO.getByOwner",query="select dslo from JPADataSetLO dslo where dslo.owner=:owner")
```
Object `DataSetDAO` uses function `AbstractDAO.getByTypedNamedQuery("DataSetLO.getByOwner", "owner", user)` to fill parameter `owner` - in named query with prefix `:` - with object `user` and retrieve the result.

##### Criteria Queries

Criteria API is the third way to create queries on JPA entities. While one named query must have all parameters filled, queries created by Criteria API are very flexible. The whole query can be dynamically generated during the runtime, that is very similar to create a String representation of JPQL query, but Criteria Queries maintain full type correctness.   
The following code snippet can serve as an example, that returns all datasets, that have hash value different from any value in the list `hashesToBeExcluded`:

```java
     	CriteriaBuilder cb= em.getCriteriaBuilder();
 		CriteriaQuery<JPADataSetLO> cq=cb.createQuery(JPADataSetLO.class);
        Root<JPADataSetLO> r=cq.from(JPADataSetLO.class);
        
        Predicate p=cb.conjunction();
        for(String exHash:hashesToBeExcluded){
            p=cb.and(p,cb.equal(r.get("hash"),exHash).not());
        }
        p=cb.and(p,cb.isNotNull(r.get("globalMetaData")));
        cq=cq.where(p);
```

Queries using Criteria API were created mainly for web interface, where it is necessary because of record sorting in views, that can be based on several column names.


### Notes for JPA

Documentation for JPA and for annotations can be found in link above. For better clarity we add some notes to some annotations used in Pikater:
* `@Lob` - as we mentioned before, we solved storage of large files using PostgreSQL Large Objects, but smaller files can be stored using this JPA annotation. In this case the variable containing the content of file is serialised and saved to a byte array - or other compatible type - in the database. This approach is used for saving computation models or XML documents.
* `@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS` or `InheritanceType.JOINED`) - this annotation is used to mark the way tables are created for classes in inheritance tree. Setting `TABLE_PER_CLASS` implies, that each class gets its table with all - inherited and implemented in particular class -variables in the class. Setting `JOINED` implies, that only one table is created for root class and classes inherited from this root, meaning, that some values might be empty.
* `@Enumerated(EnumType.STRING)` - for variables of type `java.lang.Enum` we can define, how their value is saved to the database. We can choose between string (`EnumType.STRING`) or ordinal (`EnumType.ORDINAL`) representation. We found string representation more robust, because later creation of a new value can't damage conditions on enum's value. Removing some item can cause exception when retrieving data for the database, which can be omitted by using `EnumType.ORDINAL`. However, this could lead to more obscure errors, when seemingly everything is fine, but the enum values are wrongly mapped.

