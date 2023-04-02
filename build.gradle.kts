plugins {
    id("java")
    id("application")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.6")
    implementation("com.google.cloud:google-cloud-translate:1.88.0")
    implementation("com.github.Walkyst:lavaplayer-fork:1.3.96")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.github.Doomsdayrs:Jikan4java:v1.4.2")
    implementation("org.json:json:20210307")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("net.lingala.zip4j:zip4j:2.8.0")
}

application {
    mainClass.set("bot.App")
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}
