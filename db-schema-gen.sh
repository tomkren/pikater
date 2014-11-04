#!/bin/bash

read -s -p "Enter Password: " MYPSWD
echo

# NOTE: this command requires "GraphViz" tool ("dot" command) to be available in PATH.
java -jar lib/schemaSpy_5.0.0.jar -t pgsql -db pikater -s public -host nassoftwerak.ms.mff.cuni.cz -port 5432 -u pikater -p $MYPSWD -dp lib/postgresql-9.3-1101.jdbc41.jar -o schemaspy_output