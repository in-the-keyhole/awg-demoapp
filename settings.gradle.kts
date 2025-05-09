rootProject.name = "awg-sm"

include(":awg-sm-shared")
include(":awg-sm-api")
include(":awg-sm-web")
include(":awg-sm-chart")

project(":awg-sm-shared").projectDir = file("src/awg-sm-shared")
project(":awg-sm-api").projectDir = file("src/awg-sm-api")
project(":awg-sm-web").projectDir = file("src/awg-sm-web")
project(":awg-sm-chart").projectDir = file("src/awg-sm-chart")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
