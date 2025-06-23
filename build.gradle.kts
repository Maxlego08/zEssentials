plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.0.0-beta11"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16" apply false
}

group = "zEssentials"
version = "1.0.2.6"

extra.set("targetFolder", file("target/"))
extra.set("apiFolder", file("target-api/"))
extra.set("classifier", System.getProperty("archive.classifier"))
extra.set("sha", System.getProperty("github.sha"))

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")

    group = "fr.maxlego08.essentials"
    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()

        maven(url = "https://jitpack.io")
        maven(url = "https://repo.papermc.io/repository/maven-public/")
        maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    java {
        withSourcesJar()

        if (!project.path.startsWith(":NMS:")) {
            withJavadocJar()
        }
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    tasks.javadoc {
        options.encoding = "UTF-8"
    }

    dependencies {
        compileOnly(files("libs/zMenu-1.1.0.0.jar"))

        implementation("com.github.technicallycoded:FoliaLib:0.4.3")
        implementation("com.github.Maxlego08:Sarah:1.17")
        implementation("fr.mrmicky:fastboard:2.1.4")
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

        rootProject.extra.properties["sha"]?.let { sha ->
            archiveClassifier.set("${rootProject.extra.properties["classifier"]}-${sha}")
        } ?: run {
            archiveClassifier.set(rootProject.extra.properties["classifier"] as String?)
        }
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