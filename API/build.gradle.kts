plugins {
    alias(libs.plugins.groupez.publish)
}

rootProject.extra.properties["sha"]?.let { sha ->
    version = sha
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(files("../libs/zMenu-1.1.1.6.jar"))

    implementation(libs.sarah)
    implementation(libs.folialib)
    implementation(libs.fastboard)
}

tasks {
    shadowJar {
        relocate("com.tcoded.folialib", "fr.maxlego08.essentials.libs.folialib")
        relocate("fr.maxlego08.sarah", "fr.maxlego08.essentials.libs.sarah")
        relocate("fr.mrmicky.fastboard", "fr.maxlego08.essentials.libs.fastboard")

        destinationDirectory.set(rootProject.extra["apiFolder"] as File)
    }

    build {
        dependsOn(shadowJar)
    }
}
