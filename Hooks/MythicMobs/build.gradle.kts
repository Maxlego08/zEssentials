group = "Hooks:MythicMobs"

repositories {
    maven(url = "https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":API"))
    compileOnly(libs.paper.api)
    compileOnly(libs.mythicmobs)
}
