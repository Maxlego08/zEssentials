plugins {
    alias(libs.plugins.paperweight)
}

group "NMS:V1_21_11"

dependencies {
    compileOnly(project(":API"))
    compileOnly(libs.adventure.api)
    paperweight.devBundle(libs.paperDevBundle12111)
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

