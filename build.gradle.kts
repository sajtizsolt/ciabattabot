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
    implementation(libs.lavalink.youtube.v2)
    implementation(libs.lavaplayer)
    implementation(libs.logback.classic)
    implementation(libs.logback.core)
    implementation(libs.logstash.logback.encoder)

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
        val ciabattaBotCommandPrefix: String? by project
        val ciabattaBotDiscordBotToken: String by project
        val ciabattaBotPoToken: String by project
        val ciabattaBotPoVisitorData: String by project
        group = "docker"
        description = "Starts the docker-compose.yml file"
        environment = environment + mapOf(
            "CIABATTA_BOT_COMMAND_PREFIX" to ciabattaBotCommandPrefix,
            "CIABATTA_BOT_DISCORD_BOT_TOKEN" to ciabattaBotDiscordBotToken,
            "CIABATTA_BOT_PO_TOKEN" to ciabattaBotPoToken,
            "CIABATTA_BOT_PO_VISITOR_DATA" to ciabattaBotPoVisitorData,
        )
        commandLine = "docker compose -f ${project.rootDir}\\docker-compose.yml up -d --build".split(" ")
    }
}
