plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.0.0-beta11"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16" apply false
}

group = "zEssentials"
version = project.version

extra.set("targetFolder", file("target/"))
extra.set("apiFolder", file("target-api/"))

allprojects {
    apply(plugin = "java-library")

    repositories {
        mavenLocal()
        mavenCentral()

        maven(url = "https://jitpack.io")
        maven(url = "https://repo.papermc.io/repository/maven-public/")
        maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    tasks.javadoc {
        options.encoding = "UTF-8"
    }

    dependencies {
        compileOnly("com.github.maxlego08:zMenu-API:1.0.3.8")

        implementation("com.github.technicallycoded:FoliaLib:0.4.3")
        implementation("com.github.Maxlego08:Sarah:1.15")
        implementation("fr.mrmicky:fastboard:2.1.3")
    }
}

dependencies {
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")

    api(project(":API"))

    api(project(":NMS:V1_20_4", configuration = "reobf"))
    api(project(":NMS:V1_20_6", configuration = "reobf"))
    api(project(":NMS:V1_21", configuration = "reobf"))
    api(project(":NMS:V1_21_1", configuration = "reobf"))
    api(project(":NMS:V1_21_3", configuration = "reobf"))
    api(project(":NMS:V1_21_4", configuration = "reobf"))

    rootProject.subprojects.filter { it.path.startsWith(":Hooks:") }.forEach { subproject ->
        api(project(subproject.path))
    }
}

tasks {
    shadowJar {

        relocate("com.tcoded.folialib", "fr.maxlego08.essentials.libs.folialib")
        relocate("fr.maxlego08.sarah", "fr.maxlego08.essentials.libs.sarah")
        relocate("fr.mrmicky.fastboard", "fr.maxlego08.essentials.libs.fastboard")

        archiveClassifier = ""
        // minimize()
        manifest {
            attributes["paperweight-mappings-namespace"] = "spigot"
        }

        archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
        destinationDirectory.set(rootProject.extra["targetFolder"] as File)
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(project.components["java"])
        }
    }
}