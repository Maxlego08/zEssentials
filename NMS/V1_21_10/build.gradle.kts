plugins {
    alias(libs.plugins.paperweight)
}

group "NMS:V1_21_10"

dependencies {
    compileOnly(project(":API"))
    paperweight.devBundle(libs.paperDevBundle12110)
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

