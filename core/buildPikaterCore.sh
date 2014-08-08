#!/bin/sh

dir="`dirname $0`/.."
cd "$dir"

ant -buildfile build-core.xml build;
