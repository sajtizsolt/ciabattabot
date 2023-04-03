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
        maven(url = "https://dl.bintray.com/sedmelluq/com.sedmelluq")
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "CiabattaBot"
