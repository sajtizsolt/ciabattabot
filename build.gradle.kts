@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradleup.shadow)
}

kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation(libs.apache.commons.lang3)
    implementation(libs.discord.api)
    implementation(libs.lavalink.youtube.v2)
    implementation(libs.lavaplayer)
    implementation(libs.libdave.adapter.jda)
    implementation(libs.libdave.impl.jni)
    runtimeOnly(libs.libdave.natives.linux)
    runtimeOnly(libs.libdave.natives.win)
    runtimeOnly(libs.libdave.natives.darwin)
    implementation(libs.logback.classic)
    implementation(libs.logback.core)
    implementation(libs.logstash.logback.encoder)

    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.mockk)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
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
        val ciabattaBotCommandPrefix = project.findProperty("ciabattaBotCommandPrefix")?.toString() ?: "!"
        val ciabattaBotDiscordBotToken = project.findProperty("ciabattaBotDiscordBotToken")?.toString() ?: ""
        val ciabattaBotPoToken = project.findProperty("ciabattaBotPoToken")?.toString() ?: ""
        val ciabattaBotPoVisitorData = project.findProperty("ciabattaBotPoVisitorData")?.toString() ?: ""
        group = "docker"
        description = "Starts the docker-compose.yml file"
        environment(
            mapOf(
                "CIABATTA_BOT_COMMAND_PREFIX" to ciabattaBotCommandPrefix,
                "CIABATTA_BOT_DISCORD_BOT_TOKEN" to ciabattaBotDiscordBotToken,
                "CIABATTA_BOT_PO_TOKEN" to ciabattaBotPoToken,
                "CIABATTA_BOT_PO_VISITOR_DATA" to ciabattaBotPoVisitorData,
            )
        )
        commandLine("docker", "compose", "-f", "${project.rootDir}/docker-compose.yml", "up", "-d", "--build")
    }
}
