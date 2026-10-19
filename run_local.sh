#!/bin/bash
sbt -Dperftest.runSmokeTest=false -DrunLocal=true gatling:test
