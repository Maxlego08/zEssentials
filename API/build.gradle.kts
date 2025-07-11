plugins {
    id("re.alwyn974.groupez.publish") version "1.0.0"
}

rootProject.extra.properties["sha"]?.let { sha ->
    version = sha
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly(files("../libs/zMenu-1.1.0.0.jar"))

    implementation("fr.maxlego08.sarah:sarah:1.18")
    implementation("com.tcoded:FoliaLib:0.5.1")
    implementation("fr.mrmicky:fastboard:2.1.4")
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
