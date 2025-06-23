import java.util.Locale

plugins {
    `maven-publish`
}

group "API"

rootProject.extra.properties["sha"]?.let { sha ->
    version = sha
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly(files("../libs/zMenu-1.1.0.0.jar"))

    implementation("com.github.technicallycoded:FoliaLib:0.4.3")
    implementation("com.github.Maxlego08:Sarah:1.17")
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

publishing {
    var repository = System.getProperty("repository.name", "snapshots").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    repositories {
        maven {
            name = "groupez${repository}"
            url = uri("https://repo.groupez.dev/snapshots")
            credentials {
                username = findProperty("${name}Username") as String? ?: System.getenv("MAVEN_USERNAME")
                password = findProperty("${name}Password") as String? ?: System.getenv("MAVEN_PASSWORD")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        register<MavenPublication>("groupezSnapshots") {
            pom {
                groupId = project.group as String?
                name = "${rootProject.name}-${project.name}"
                artifactId = name.get().lowercase()
                version = project.version as String?

                scm {
                    connection = "scm:git:git://github.com/MaxLego08/${rootProject.name}.git"
                    developerConnection = "scm:git:ssh://github.com/MaxLego08/${rootProject.name}.git"
                    url = "https://github.com/MaxLego08/${rootProject.name}/"
                }
            }
            artifact(tasks.shadowJar)
            artifact(tasks.javadocJar)
            artifact(tasks.sourcesJar)
        }
    }
}
