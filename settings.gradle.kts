rootProject.name = "awg-storemaster"

include(":awg-storemaster-shared")
include(":awg-storemaster-api")
include(":awg-storemaster-web")
include(":awg-storemaster-chart")

project(":awg-storemaster-shared").projectDir = file("src/awg-storemaster-shared")
project(":awg-storemaster-api").projectDir = file("src/awg-storemaster-api")
project(":awg-storemaster-web").projectDir = file("src/awg-storemaster-web")
project(":awg-storemaster-chart").projectDir = file("src/awg-storemaster-chart")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

pluginManagement {

}
