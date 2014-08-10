#!/bin/sh

dir="`dirname $0`/.."
cd "$dir"

ant -buildfile buildCore.xml build;
