description = "awg-sm-api"

plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.4.5"
    id("com.google.cloud.tools.jib") version "3.4.5"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(project(":awg-sm-shared"))
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.4")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.4") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.4.4")
    implementation(platform("com.azure.spring:spring-cloud-azure-dependencies:5.22.0"))
    implementation("com.azure.spring:spring-messaging-azure-servicebus:5.22.0")
}

tasks.withType<com.google.cloud.tools.jib.gradle.JibTask>().configureEach {
    notCompatibleWithConfigurationCache("because https://github.com/GoogleContainerTools/jib/issues/3132")
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    args("--spring.profiles.active=local")
}
