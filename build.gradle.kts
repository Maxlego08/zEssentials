plugins {
    `java-library`
    alias(libs.plugins.shadow)
    alias(libs.plugins.paperweight) apply false
    alias(libs.plugins.groupez.repository)
}

group = "fr.maxlego08.essentials"
version = "1.0.4.0"

extra.set("targetFolder", file("target/"))
extra.set("targetFolderDiscord", file("target-discord/"))
extra.set("apiFolder", file("target-api/"))
extra.set("classifier", System.getProperty("archive.classifier"))
extra.set("sha", System.getProperty("github.sha"))

val rootLibs = libs
allprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "re.alwyn974.groupez.repository")

    group = "fr.maxlego08.essentials"
    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()

        maven(url = "https://jitpack.io")
        maven(url = "https://repo.papermc.io/repository/maven-public/")
        maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven(url = "https://repo.tcoded.com/releases")
        maven {
            name = "faststatsReleases"
            url = uri("https://repo.faststats.dev/releases")
        }
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "net.kyori" && requested.name == "adventure-text-serializer-ansi" && (requested.version.isNullOrBlank() || requested.version == ".")) {
                useVersion(rootLibs.versions.adventure.text.serializer.ansi.get())
            }
        }
    }

    java {
        withSourcesJar()

        // Allow the Java 21 main module to depend on the Java 25 NMS module for Minecraft 26.x.
        disableAutoTargetJvm()

        if (!project.path.startsWith(":NMS:")) {
            withJavadocJar()
        }
    }

    tasks.shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveAppendix.set(if (project.path == ":") "" else project.name)
        archiveClassifier.set("")
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
        options.release = 21
    }

    tasks.javadoc {
        options.encoding = "UTF-8"
        if (JavaVersion.current().isJava9Compatible)
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }

    dependencies {
        compileOnly(files("libs/zMenu-1.1.1.6.jar"))

        compileOnly(rootLibs.sarah)
        compileOnly(rootLibs.folialib)
        compileOnly(rootLibs.fastboard)
    }
}

dependencies {
    compileOnly(libs.placeholderapi)
    compileOnly(libs.paper.api)

    implementation(libs.fastStatsBukkit) {
        exclude(group = "com.google.code.gson", module = "gson")
    }

    api(project(":API"))

    // Since Minecraft 1.20.5 Paper ships a Mojang-mapped runtime, and since 26.1 Spigot
    // reobfuscation is gone entirely. Every NMS module is therefore Mojang-mapped
    // (MOJANG_PRODUCTION) and consumed as a plain project dependency (no "reobf" variant).
    api(project(":NMS:V1_21_4"))
    api(project(":NMS:V1_21_5"))
    api(project(":NMS:V1_21_6"))
    api(project(":NMS:V1_21_7"))
    api(project(":NMS:V1_21_8"))
    api(project(":NMS:V1_21_9"))
    api(project(":NMS:V1_21_10"))
    api(project(":NMS:V1_21_11"))
    api(project(":NMS:V26_2"))
    api(project(":NMS:V26_3"))

    rootProject.subprojects.filter { it.path.startsWith(":Hooks:") }.forEach { subproject ->
        api(project(subproject.path))
    }
}

tasks {
    shadowJar {
        relocate("com.tcoded.folialib", "fr.maxlego08.essentials.libs.folialib")
        relocate("fr.maxlego08.sarah", "fr.maxlego08.essentials.libs.sarah")
        relocate("fr.mrmicky.fastboard", "fr.maxlego08.essentials.libs.fastboard")
        relocate("dev.faststats", "fr.maxlego08.essentials.libs.faststats")

        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
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

    processResources {
        from("resources")
        filesMatching(listOf("plugin.yml", "paper-plugin.yml")) {
            expand("version" to project.version)
        }
    }
}
