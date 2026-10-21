group = "Hooks:WorldGuard"

repositories {
    maven(url = "https://maven.enginehub.org/repo/")
}

dependencies {

    compileOnly(project(":API"))
    compileOnly(libs.paper.api)
    compileOnly(libs.worldguard)
    compileOnly(libs.worldedit)
}