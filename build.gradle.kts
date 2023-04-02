@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    java
    alias(libs.plugins.kotlin.jvm)
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

    testRuntimeOnly(libs.junit.jupiter.engine)
}

application {
    mainClass.set("bot.MainKt")
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}
