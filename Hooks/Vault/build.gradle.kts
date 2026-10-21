group = "Hooks:Vault"

dependencies {
    compileOnly(project(":API"))
    compileOnly(libs.vault) {
        exclude(group = "org.bukkit", module = "bukkit")
    }
    compileOnly(libs.paper.api)
}