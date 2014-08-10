#!/bin/sh

if [ $# -ne 1 ]; then
  echo "usage: $0 <name-of-slave-node>" >&2
  exit 1
fi

cd "`dirname $0`/.."

ant -buildfile buildCore.xml -Darg0=$1 PikaterSlave
