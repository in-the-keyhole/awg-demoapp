plugins {
    id("java")
    id("java-library")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

description = "awg-sm-shared"
