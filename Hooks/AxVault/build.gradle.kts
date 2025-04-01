group = "Hooks:AxVault"

dependencies {

    compileOnly(project(":API"))
    compileOnly(files("libs/AxVaults.jar"))
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
}