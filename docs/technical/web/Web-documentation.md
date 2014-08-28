<!-- --- title: Web application documentation-->

[[_TOC_]]




## Main technologies used

The focus of this documentation is not to explain the technologies used or provide documentation to them. If you wish to read more about them, follow the links or take a look at the "[Other useful links](#webLinks)" section below. A light introduction, however:
* [Google Web Toolkit](http://www.gwtproject.org/) (GWT).  
GWT is mainly a client-side (browser) technology. It allows developing web applications in java - the code is then compiled and translated into javascript.  
* [Vaadin](https://vaadin.com/book/-/page/preface.html) framework based on GWT to implement GUI.  
Vaadin is both client-side (extending GWT) and server-side technology, and moves web application development to server-side. Events like user clicking on a button are transferred and handled on the server (AJAX), as opposed to standalone GWT which (by default) handles events on the client. Requests to server are, with some exceptions of course, in the form of AJAX calls. For more information, refer to the Book of Vaadin.  
GWT and Vaadin form the infrastructure and looks of our web application.
* [KineticJS](https://github.com/ericdrowell/KineticJS) to draw experiments on the client.  
KineticJS is javascript library used for drawing custom content (styled shapes, curves, text, animations, etc.) in a HTML5's canvas element. It is used in the GWT part of our application to define a widget (GWT term) that draws experiments in the browser.
* [KineticGWT](https://github.com/neothemachine/KineticGWT) for nice & easy integration of KineticJS in GWT.  
KineticGWT is a GWT wrapper for KineticJS and is used to simplify development - it uses JSNI (inner GWT technology) to provide java interface and call javascript on the background.




## Configuration

All available options can be found and customized in: `WebContent/WEB-INF/conf/appServer.properties`. At this moment, only one option is allowed:

**Online/offline mode**  
The application is in "online" mode when it communicates with an instance of system core, so:
* scheduled experiments are immediately delegated to system core for execution,
* newly uploaded agents are immediately delegated to system core for processing,
* newly uploaded datasets are immediately delegated to system core for registration.
* various other changes are immediately delegated to system core.

Online mode means the application is in its full capabilities.  
On the contrary, in offline mode only database is available so one can only view/edit the database state. Changes are saved to database and a dialog pops up informing the user that they will not be fully handled until a core system instance picks them up. As of the experiment editor, it will contain dummy boxes.

## Main processes

### Startup process

Should be clear from the following diagram:

{{{{{{ blue-modern
	Web app->Web app: initialize logger, inner state & Quartz
	Web app->Web app: parse & store web-app configuration
	alt web-app is configured to run in online mode
		Web app->Core system: RPC to test availability
		Core system->Web app: response
	end
	Web app->Database: init connection to test availability
	Database->Web app: response
}}}}}}

But you can refer to the latest source code as well: `org.pikater.web.StartupAndQuitListener.java`.  
If any errors occur during this phase, the application will not start. Errors will need to be resolved before it can be successfully launched.

### Page request handling

Has quite standard implementation:

{{{{{{ blue-modern
	Client->Web app: request page	
	alt user is not logged in
		Web app->Web app: output login dialog
	else user is logged in
		Web app->Requested-page: request page
		alt access is allowed
			Requested-page->Web app: output requested page
		else access is not allowed
			Requested-page->Web app: output error message
		end
	end
	Web app->Client: return response
}}}}}}

Since the web application is an AJAX application (javascript calls do not refresh the whole page), subsequent calls from javascript do not apply to the scheme above. They are simply handled in RPC manner on the server. For more information about how this is done, refer to Book of Vaadin.




## GUI documentation<a name="webGUI"/>

See [[user guide|User-guide]].




## Other useful links<a name="webLinks"/>

* [Servlet 3.0 documentation](http://tomcat.apache.org/tomcat-7.0-doc/servletapi/)




