group = "Hooks:MythicMobs"

repositories {
    maven(url = "https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":API"))
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly("io.lumine:Mythic-Dist:5.7.2")
}
