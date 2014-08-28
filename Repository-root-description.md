<!-- --- title: Repository description -->

## Source code

It is contained in the `src` and `examples` folders. The latter only contains examples of used 3rd party software (dependencies).

## Application dependencies

They are contained in the `libraries` folder and `ivy.xml` file ([Apache Ivy](http://ant.apache.org/ivy) dependency management).

## Documentation

This project contains an inline documentation written in markdown and easily available as a wiki. For more information, check the [[technical documentation|Technical-documentation]].  

### MarkDown files

Home page is represented by the `Home.md` file. From there, links can be followed to other documentation files, most of which are contained in the `docs` folder and a few others are located in the `src` folder.

### Javadoc

The repository contains Javadoc generated to all source code in the `src` folder and is located in the `javadoc` folder. MarkDown files link to it as well.

### Wiki

Several other files in the repository's root are related to the aforementioned Wiki. Namely:

1. `custom.css`  
Contains custom CSS styles that override the Wiki's default styles to make it a bit nicer to work with.
2. `wiki-config.rb`
Contains configuration for the wiki.
3. `wiki-launch.sh`  
Startup script for the wiki. See [[how to launch wiki|02-Launching]].