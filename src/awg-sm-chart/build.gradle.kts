plugins {
    id("com.citi.helm") version "2.2.0"
    id("com.palantir.git-version") version "3.2.0"
}

val gitVersion: groovy.lang.Closure<String> by extra
version = gitVersion()
description = "awg-sm-chart"
