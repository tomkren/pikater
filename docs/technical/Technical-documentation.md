<!-- --- title: Technical documentation -->

[[_TOC_]]




## Architecture

<font color="red">TODO: is this alright or something needs to be edited/added?</font>

[[architecture.png|architecture picture]]




## Individual components/modules

### Database<a name="database"/>

[[Open database documentation|Database-documentation]].

### Core system<a name="core"/>

[[Open core system documentation|Core-documentation]].

### Web application<a name="web"/>

[[Open web application documentation|Web-documentation]].




## Source code<a name="sourceCode"/>

### Package structure

To display relevant information regarding folders/packages of the source code, follow the links below:

1. [[Repository root folder|Repository-root-description]]
2. [[Default source code package|Default-package-description]]

From there, you can navigate further down the tree.  
Alternatively, you may enter the [fileview](/fileview) and browse the `src` folder. Some of the packages contain a description file.

### Javadoc<a name="javadoc"/>

* [Open Javadoc](../../javadoc/index.html)




## Logging

Application doesn't use unified logging technology, implementation or interface at this moment. Individual application components use their own. They are all defined in the `org.pikater.shared.logging` package. Each application component has its own subpackage defined.

An attempt has been made to centralize logging into the servlet container but has not been fully implemented/used after all. For more information, refer to [[default package description|Default-package-description]].

## Wiki

The project contains an inline documentation that can easily be displayed as a wiki. For information on how to install, deploy & use it, refer to the [[documentation for administrators|Admin-guide]].

### Involved technologies

1. [Ruby Version Management (RVM)](https://rvm.io/)  
	Ruby is a specification of a programming language and there are many implementations of it. RVM aims (beside other things) to reduce the efforts needed to develop/test in specific ruby versions & implementations. It is not necessary to install it on an environment where no such activities occur.

2. [Gollum wiki and its dependencies](https://github.com/gollum/gollum)  
	It is a wiki engine built on top of Ruby, Python and Rack. GitHub itself uses an adjusted version of Gollum for their repository wikis. It has many capabilities but most importantly - it is git-based which means one can create an inline documentation with the code itself. One big catch though - **__it doesn't support Windows at this moment__**.

While the inline documentation is more or less wiki-independent (markdown format), this repository is configured to be used with Gollum.
	
### Configuration files

Configuration files for the wiki can be found at the repository's root folder. See the [source code](#sourceCode) section.

