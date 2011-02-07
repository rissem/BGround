#!/bin/sh
../dep/VLC.app/Contents/MacOS/VLC -I http --http-src ../vlcHttpInterface --http-host 127.0.0.1:8081 &
../apache-tomcat-6.0.29/bin/startup.sh &
