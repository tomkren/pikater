<!-- --- title: Application package -->

`org.pikater` - contains all application code.

## Core package
Core system is defined in this package, which means all Jade-related code that executes experiments and manages agents and their infrastructure. Documentation of core system can be found [[here|Technical-documentation#core]].

## Shared

Various shared classes and frameworks are contained in this package. "Shared" actually means shared between `core` and `web`.

Main functionalities of this package include:
* Database framework.  
Connection definition & providers, entities, queries, etc.
* Quartz framework.  
Provides a mechanism for executing background tasks. It is only used by `web` at this moment but can easily be used by `core` as well.
* Universal experiment format.  
It acts as a mediator between `web` and `core` experiments formats, because they are quite different and need to address different requirements. Experiments saved in database are also in the universal format.
* Various utilities and logging interfaces/implementations.

## Web

All web application related code is concentrated in this package, with the exception of shared code.

More detailed information about this package can be found [[here|Web-package-description]].

## Dependencies

Dependencies are represented by the following diagram:

{{{{{{ blue-modern
	Core->Shared: requires
	Web->Shared: requires
	alt web-app is in online mode
		Web->Core: requires
	end
}}}}}}

`Core` and `shared` packages are and aim to be completely independent of `web`, but as a standalone unit, they are also very limited in usage and user-experience.

`Web` is only dependent on `core` when it is in "online mode" (see [[web application documentation|Web-documentation]]). To be precise, `web` only calls methods defined in `org.pikater.core.agents.gateway.WebToCoreEntryPoint.java`.