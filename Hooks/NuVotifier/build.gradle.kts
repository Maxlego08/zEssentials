group = "Hooks:NuVotifier"

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {

    compileOnly(project(":API"))
    compileOnly(files("libs/nuvotifier.jar"))
    compileOnly(libs.paper.api)
}