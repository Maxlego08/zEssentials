plugins {
    alias(libs.plugins.paperweight)
}

group "NMS:V26_2"

dependencies {
    compileOnly(project(":API"))
    compileOnly(libs.adventure.api.latest)
    paperweight.devBundle(libs.paperDevBundle262)
}

// Minecraft 26.x is Mojang-mapped only (no Spigot reobfuscation since 26.1), so the default
// production artifact is already Mojang-mapped and no reobfArtifactConfiguration is required.

java {
    // Minecraft 26.x targets Java 25.
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}
