description = "awg-storemaster"

plugins {
    id("buildlogic.common")
    id("distribution")
}

distributions {
    main {
        contents {
            from()
        }
    }
}

tasks.register<Copy>("distWeb") {
    val task = project(":awg-storemaster-web").tasks.named("jibBuildTar")
    dependsOn(task)

    from(project(":awg-storemaster-web").layout.buildDirectory.get().file("jib-image.tar"))
    rename { "awg-storemaster-web.tar" }
    into(layout.projectDirectory.dir("dist").dir("images"))
}

tasks.register<Copy>("distApi") {
    val task = project(":awg-storemaster-api").tasks.named("jibBuildTar")
    dependsOn(task)

    from(project(":awg-storemaster-api").layout.buildDirectory.get().file("jib-image.tar"))
    rename { "awg-storemaster-api.tar" }
    into(layout.projectDirectory.dir("dist").dir("images"))
}

tasks.register<Copy>("distChart") {
    val task = project(":awg-storemaster-chart").tasks.named("helmPackage");
    dependsOn(task)
    from(project(":awg-storemaster-chart").layout.buildDirectory.get().dir("helm").dir("charts")) {
        include("awg-storemaster-chart-*.tgz")
    }
    into(layout.projectDirectory.dir("dist").dir("charts"))
}

tasks.register<Copy>("dist") {
    dependsOn("distWeb")
    dependsOn("distApi")
    dependsOn("distChart")
}
