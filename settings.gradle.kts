pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven(url = "https://m2.dv8tion.net/releases")
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.lavalink.dev/releases")
    }
}

rootProject.name = "CiabattaBot"
