plugins {
    id("io.papermc.paperweight.userdev")
}

group "NMS:V1_21_8"

dependencies {
    compileOnly(project(":API"))
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
    // Fix for missing adventure-text-serializer-ansi dependency
    compileOnly("net.kyori:adventure-text-serializer-ansi:4.17.0")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}