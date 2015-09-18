#!/bin/bash
#
# The purpose of this script is to produce the vidivox.jar artifact which can
# be run by runme.sh

cd src
/usr/lib/jvm/jdk8/bin/javac -cp . player/*.java
/usr/lib/jvm/jdk8/bin/jar cvfm vidivox.jar ../manifest.txt player/*.class
