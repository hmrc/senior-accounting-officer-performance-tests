#!/bin/bash
sbt '; clean; set javaOptions ++= Seq("-DbaseUrl=www.staging.tax.service.gov.uk", "-Dperftest.runSmokeTest=false", "-DrunLocal=false"); gatling:test'

