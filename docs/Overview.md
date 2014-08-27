<!-- --- title: Documentation -->

[[_TOC_]]

## Overview

For an overview of what this application aims to achieve, check the project's [[home|Home]].

## Application

### Architecture

<font color="red">// TODO: update the picture to reflect most recent changes</font>

See [this](https://drive.google.com/file/d/0B8OZ5gPpSOKQejBscXBMTWxJR0U/edit?usp=sharing) picture.

### Individual components/modules

#### Core

<font color="red">// TODO</font>

* [Jade documentation](http://jade.tilab.com/doc/)

#### Web application

##### Principal technologies used

The goal of this documentation is not to fully explain the technologies used. If you wish to read more about them, follow the links below or take a look at the 'Other useful links' section in this chapter. A light introduction, however:

As stated in the project's [[home|Home]], the main web technologies used are:
* [Google Web Toolkit (GWT)](http://www.gwtproject.org/)
* [Vaadin](https://vaadin.com/book/-/page/preface.html)
* [KineticJS](https://github.com/ericdrowell/KineticJS)
* [KineticGWT](https://github.com/neothemachine/KineticGWT)

GWT is mainly a client-side (browser) technology. It allows developing web applications in java - the code is then compiled and translated into javascript.  
Vaadin is both client-side (extending GWT) and server-side technology, and moves web application development to server-side. Events like user clicking on a button are transfered and handled on the server, as opposed to standalone GWT which (by default) handles events on the client. For more information, refer to the Book of Vaadin.  
GWT and Vaadin form the infrastructure and looks of our web application.

KineticJS is javascript library used for drawing custom content (styled shapes, curves, text, animations, etc.) in a HTML5's canvas element. It is used in the GWT part of our application to define a widget (GWT term) that draws experiments in the browser.
KineticGWT is a GWT wrapper for KineticJS and is used to simplify development - it uses JSNI (inner GWT technology) to provide java interface and call javascript on the background.

##### Workflow

{{{{{{ blue-modern
  Client->Server: request page
  Server->Server: output login dialog if user is not logged in
  Server->Server: output application launch wizard if application has not been launched yet
  Server->Requested-page: if user is logged in and application has been launched
  Requested-page->Server: output requested page
  Server->Client: return response
}}}}}}

To display summary of what is returned to client requests, click [[here|Web interface summary]].

##### Other useful links

* [Servlet 3.0 documentation](http://tomcat.apache.org/tomcat-7.0-doc/servletapi/)

<font color="red">// TODO</font>
* [JSch javadoc](http://epaul.github.io/jsch-documentation/javadoc/)

#### Database

<font color="red">// TODO</font>

* [Java Quartz Scheduler (cron jobs) documentation](http://www.quartz-scheduler.org/documentation)
* [Java Quartz Scheduler configuration reference](http://www.quartz-scheduler.org/documentation/quartz-2.2.x/configuration)
* [Java persistence API](http://docs.oracle.com/javaee/5/tutorial/doc/bnbpz.html)
* [Using Java Persistence API](http://docs.oracle.com/cd/E16439_01/doc.1013/e13981/cmp30cfg.htm)

### Logging

<font color="red">// TODO</font>

### Deployment & launching

For information about how to deploy & launch the application, consult the project's [[home|Home]].

In our case, we used [Apache Tomcat](http://tomcat.apache.org/tomcat-7.0-doc/) to deploy the application.

### Maintenance

The application should be self-maintained.  
In case of problems, refer to the logs (see the [Logging](#Logging) section).

### Source code

#### Package structure

To display relevant information regarding the package structure of the source code, click [here](/fileview) and browse the `src` folder. Many of the packages contain a description file containing relevant information about the package it is in.

To display description of the default package (files in the `src` folder), click [[here|Default package description]].

#### Javadoc

* [Open Javadoc](../javadoc/index.html)

## Wiki

The project contains an inline documentation that can easily be displayed as a wiki. 

### Involved technologies

1. [Ruby Version Management (RVM)](https://rvm.io/)  
	Ruby is a specification of a programming language and there are many implementations of it. RVM aims (beside other things) to reduce the efforts needed to develop/test in specific ruby versions & implementations. It is not necessary to install it on an environment where no such activities occur.

2. [Gollum wiki and its dependencies](https://github.com/gollum/gollum)  
	It is a wiki engine built on top of Ruby, Python and Rack. GitHub itself uses an adjusted version of Gollum for their repository wikis. It has many capabilities but most importantly - it is git-based which means one can create an inline documentation with the code itself. One big catch though - <font color="red">it doesn't support Windows at this moment</font>.

While the inline documentation is wiki-independent, this repository is configured to be used with Gollum.
	
### Configuration files

Configuration files for the wiki can be found at the repository's root directory. See [[Information about this repository|Home#Information about this repository]].
	
### How to install

Click [[here|01-Installation]].

### How to launch

Click [[here|02-Launching]].

### How to use

Click [[here|03-Usage]].


