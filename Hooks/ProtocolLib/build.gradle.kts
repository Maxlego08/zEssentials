group = "Hooks:ProtocolLib"

repositories {
    maven(url = "https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly(project(":API"))
    compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0")
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
}