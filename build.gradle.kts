@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.johnrengelman.shadow)
}

dependencies {
    implementation(libs.apache.commons.lang3)
    implementation(libs.discord.api)
    implementation(libs.google.cloud.translate)
    implementation(libs.lavaplayer.fork)
    implementation(libs.logback.classic)
    implementation(libs.jikan4java)
    implementation(libs.json)
    implementation(libs.zip4j)

    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.mockk)

    testRuntimeOnly(libs.junit.jupiter.engine)
}

application {
    mainClass.set("bot.MainKt")
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType(JavaCompile::class) {
        options.encoding = "UTF-8"
    }

    register<Exec>("dockerComposeUp") {
        val ciabattaBotDiscordBotToken: String by project
        group = "docker"
        description = "Starts the docker-compose.yml file"
        environment = environment + mapOf(
            "DISCORD_BOT_TOKEN" to ciabattaBotDiscordBotToken,
        )
        commandLine = "docker-compose -f ${project.rootDir}/docker-compose.yml up -d --build".split(" ")
    }
}
