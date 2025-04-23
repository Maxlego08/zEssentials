group = "Hooks:NuVotifier"

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {

    compileOnly(project(":API"))
    compileOnly(files("libs/nuvotifier.jar"))
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
}