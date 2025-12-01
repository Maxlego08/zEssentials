plugins {
    `java-library`
    id("application")
    id("com.gradleup.shadow") version "9.0.0-beta11"
}

application {
    mainClass.set("fr.maxlego08.essentials.bot.DiscordBot")
}

group = "zEssentialsDiscord"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.yaml:snakeyaml:2.3")
    implementation("net.dv8tion:JDA:5.2.0")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation(project(":API"))
}

tasks {
    shadowJar {

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
