#!/bin/bash
#
# The purpose of this script is to launch vidivox with the correct jdk.
# author: Arran Davis (adav194)

/usr/lib/jvm/jdk8/bin/java -jar src/vidivox.jar
rm -f ./temp/*.mp4
rm -f ./temp/*.mp3
