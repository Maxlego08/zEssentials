plugins {
    `java-library`
    id("application")
    alias(libs.plugins.shadow)
}

application {
    mainClass.set("fr.maxlego08.essentials.bot.DiscordBot")
}

group = "zEssentialsDiscord"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.snakeyaml)
    implementation(libs.jda)
    implementation(libs.mysql.connector)
    implementation(libs.mariadb.connector)
    implementation(project(":API"))
}

tasks {
    shadowJar {

        relocate("com.tcoded.folialib", "fr.maxlego08.essentials.libs.folialib")
        relocate("fr.maxlego08.sarah", "fr.maxlego08.essentials.libs.sarah")
        relocate("fr.mrmicky.fastboard", "fr.maxlego08.essentials.libs.fastboard")

        rootProject.extra.properties["sha"]?.let { sha ->
            archiveClassifier.set("${rootProject.extra.properties["classifier"]}-${sha}")
        } ?: run {
            archiveClassifier.set(rootProject.extra.properties["classifier"] as String?)
        }
        destinationDirectory.set(rootProject.extra["targetFolderDiscord"] as File)
    }

    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.release = 21
    }

    processResources {
        from("resources")
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
}
