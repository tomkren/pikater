<!-- --- title: Default package description -->

Contains all application code.

#Core

Core system is defined in this package, which means all Jade-related code that executes experiments, manages system and user data. Documentation of core system can be found <font color="red">here</font>.  
Has dependencies on `shared` package only.

#Shared

Various shared classes and frameworks are contained in this package. 'Shared' actually means shared between `core` and `web` which aim to be mutually independent. In reality, only `core` is completely independent of `web`, not vice versa.  
Has no dependencies on other packages.

Main functionalities of this package include:
* Database framework.  
Connection definition & providers, entities, queries, etc.
* Quartz framework.  
Provides a mechanism for executing background tasks. It is only used by `web` at this moment but can easily be used by `core` as well.
* Universal experiment format.  
It acts as a mediator between `web` and `core` experiments formats, because they are quite different and need to address different requirements. Experiments saved in database are also in the universal format.
* Various utilities and logging interfaces/implementations.

#Web

All web application related code is concentrated in this package, with the exception of shared code.  
Has dependencies on both `client` and `shared` packages.

Click <font color="red">here</font> to display more detailed information about this package.