#!/bin/sh

path="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "$path/../lib/*"
java -cp "$path/../lib/*" org.mule.me.launcher.MuleLauncher $@