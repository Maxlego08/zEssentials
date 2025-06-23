group = "Hooks:WorldGuard"

repositories {
    maven(url = "https://maven.enginehub.org/repo/")
}

dependencies {

    compileOnly(project(":API"))
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.13")
    compileOnly("com.sk89q.worldedit:worldedit-core:7.3.12")
}