group = "Hooks:SuperiorSkyBlock2"

repositories {
    maven(url = "https://repo.bg-software.com/repository/api/")
}

dependencies {
    compileOnly("com.bgsoftware:SuperiorSkyblockAPI:2024.4")
    compileOnly(project(":API"))
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
}