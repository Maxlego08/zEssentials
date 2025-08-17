pluginManagement {
    repositories {
        maven {
            name = "groupezReleases"
            url = uri("https://repo.groupez.dev/releases")
        }
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "zEssentials"

include("API")
include("Hooks:Vault")
include("Hooks:SuperiorSkyBlock2")
include("Hooks:Redis")
include("Hooks:ProtocolLib")
include("Hooks:BlockTracker")
include("Hooks:AxVault")
include("Hooks:NuVotifier")
include("Hooks:NChat")
include("Hooks:WorldGuard")

include("NMS:V1_20_4")
include("NMS:V1_20_6")
include("NMS:V1_21")
include("NMS:V1_21_1")
include("NMS:V1_21_3")
include("NMS:V1_21_4")
include("NMS:V1_21_5")
include("NMS:V1_21_6")
include("NMS:V1_21_7")
include("NMS:V1_21_8")

