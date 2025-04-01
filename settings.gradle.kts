plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "zEssentials"

include("API")

/*include 'API'

include 'Hooks:Vault'
include 'Hooks:SuperiorSkyBlock2'
include 'Hooks:Redis'
include 'Hooks:ProtocolLib'
include 'Hooks:BlockTracker'
include 'Hooks:AxVault'

include 'NMS:v1_21_4'
include 'NMS'
// include 'NMS:v1_20_4'
include 'NMS:v1_20_6'
include 'NMS:v1_21'
include 'NMS:v1_21_1'
include 'NMS:v1_21_3'
include ':DiscordBot'*/

