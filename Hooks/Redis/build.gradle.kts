group = "Hooks:Redis"

// Need rework

dependencies {
    compileOnly(project(":API"))
    implementation("redis.clients:jedis:4.3.1") {
        exclude(group = "com.google.code.gson", module = "gson")
        exclude(group = "org.slf4j", module = "slf4j-api")
    }
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
}