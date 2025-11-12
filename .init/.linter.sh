#!/bin/bash
cd /home/kavia/workspace/code-generation/target-clicker-223414-223424/backend
./gradlew checkstyleMain
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

