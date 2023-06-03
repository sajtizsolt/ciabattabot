FROM eclipse-temurin:17.0.7_7-jdk-alpine as builder

ENV GRADLE_USER_HOME /app/.gradle
ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx2g"

ARG DISCORD_BOT_TOKEN
ARG DISCORD_BOT_COMMAND_PREFIX
ARG PROJECT=ciabattabot

WORKDIR /app

COPY . .

RUN ./gradlew clean shadowJar --no-daemon \
    -Pkotlin.compiler.execution.strategy=in-process \
    -PciabattaBotDiscordBotCommandPrefix=${DISCORD_BOT_COMMAND_PREFIX} \
    -PciabattaBotDiscordBotToken=${DISCORD_BOT_TOKEN}

FROM eclipse-temurin:17.0.7_7-jre-alpine

ARG PROJECT=ciabattabot

COPY --from=builder /app/build/libs/CiabattaBot-all.jar /CiabattaBot-all.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /CiabattaBot-all.jar"]
