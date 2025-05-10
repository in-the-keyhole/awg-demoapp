description = "awg-storemaster-shared"

plugins {
    id("java")
    id("java-library")
    id("buildlogic.java-conventions")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
