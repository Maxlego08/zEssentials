plugins {
    id("io.papermc.paperweight.userdev")
}

group "NMS:V1_21_10"

dependencies {
    compileOnly(project(":API"))
    paperweight.paperDevBundle("1.21.10-R0.1-SNAPSHOT")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}