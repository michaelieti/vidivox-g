#!/bin/bash
#
# The purpose of this script is to produce the vidivox.jar artifact which can
# be run by runme.sh

cd src
/usr/lib/jvm/jdk8/bin/javac -cp . `find | grep .java$`
/usr/lib/jvm/jdk8/bin/jar cvfm vidivox.jar ../manifest.txt `find | grep .class$` `find | grep .css$` `find | grep .png$` `find | grep .mp4$`
rm -f `find | grep .class$`
