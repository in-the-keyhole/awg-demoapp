plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.bisiach.gradle.gitversion-plugin:com.bisiach.gradle.gitversion-plugin.gradle.plugin:1.0.2")
}
