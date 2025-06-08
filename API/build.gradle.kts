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
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${System.getenv("GITHUB_REPOSITORY_OWNER")}/zEssentials")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        register<MavenPublication>("gpr") {
            // https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#publishing-a-package
            pom {
                groupId = project.group as String?
                name = "${rootProject.name}-${project.name}"
                artifactId = name.get().lowercase()
                version = project.version as String?
            }
            artifact(tasks.shadowJar)
        }
    }
}
