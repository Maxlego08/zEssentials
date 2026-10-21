group = "Hooks:SuperiorSkyBlock2"

repositories {
    maven(url = "https://repo.bg-software.com/repository/api/")
}

dependencies {
    compileOnly(libs.superiorskyblock)
    compileOnly(project(":API"))
    compileOnly(libs.paper.api)
}