plugins {
    id("io.papermc.paperweight.userdev")
}

group "NMS:V1_21_10"

dependencies {
    compileOnly(project(":API"))
    compileOnly("net.kyori:adventure-api:4.26.1")
    paperweight.paperDevBundle("1.21.10-R0.1-SNAPSHOT")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}