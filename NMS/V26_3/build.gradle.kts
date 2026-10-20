plugins {
    id("io.papermc.paperweight.userdev")
}

group "NMS:V26_3"

dependencies {
    compileOnly(project(":API"))
    compileOnly("net.kyori:adventure-api:4.26.1")
    paperweight.paperDevBundle("26.3.build.+")
}

// Minecraft 26.x is Mojang-mapped only (no Spigot reobfuscation since 26.1), so the default
// production artifact is already Mojang-mapped and no reobfArtifactConfiguration is required.

java {
    // Minecraft 26.x targets Java 25.
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.withType<JavaCompile>().configureEach {
    // The Java 25 toolchain is required to read the Mojang-mapped 26.3 dev bundle, but the bytecode
    // is emitted for Java 21: the shadow plugin remaps every class with ASM when shading, and its
    // ASM version cannot read class file major version 69 (Java 25).
    options.release = 21
}
