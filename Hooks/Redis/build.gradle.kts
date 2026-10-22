group = "Hooks:Redis"

// Need rework

dependencies {
    compileOnly(project(":API"))
    implementation(libs.jedis) {
        exclude(group = "com.google.code.gson", module = "gson")
        exclude(group = "org.slf4j", module = "slf4j-api")
    }
    compileOnly(libs.paper.api)
}