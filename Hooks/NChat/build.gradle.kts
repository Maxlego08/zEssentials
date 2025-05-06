group = "Hooks:NChat"

repositories {
    maven(url = "https://repo.nickuc.com/maven-releases/")
}

dependencies {
    compileOnly(project(":API"))
    compileOnly("com.nickuc.chat:api:5.6")
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
}