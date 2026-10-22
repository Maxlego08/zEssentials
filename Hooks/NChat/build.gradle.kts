group = "Hooks:NChat"

repositories {
    maven(url = "https://repo.nickuc.com/maven-releases/")
}

dependencies {
    compileOnly(project(":API"))
    compileOnly(libs.nchat)
    compileOnly(libs.paper.api)
}