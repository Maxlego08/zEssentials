group = "Hooks:Vault"

dependencies {
    compileOnly(project(":API"))
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
}