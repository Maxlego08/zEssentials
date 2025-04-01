plugins {
    id("io.papermc.paperweight.userdev")
}

group "NMS:V1_20_4"

dependencies {
    compileOnly(project(":API"))
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}