FROM eclipse-temurin:17.0.12_7-jdk-focal as builder

ENV GRADLE_USER_HOME /app/.gradle
ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx2g"

ARG CIABATTA_BOT_DISCORD_BOT_TOKEN
ARG CIABATTA_BOT_COMMAND_PREFIX
ARG CIABATTA_BOT_PO_TOKEN
ARG CIABATTA_BOT_PO_VISITOR_DATA

WORKDIR /app

COPY . .

RUN ./gradlew clean shadowJar --no-daemon \
    -Pkotlin.compiler.execution.strategy=in-process \
    -PciabattaBotCommandPrefix=${CIABATTA_BOT_COMMAND_PREFIX} \
    -PciabattaBotDiscordBotToken=${CIABATTA_BOT_DISCORD_BOT_TOKEN} \
    -PciabattaBotPoToken=${CIABATTA_BOT_PO_TOKEN} \
    -PciabattaBotPoVisitorData=${CIABATTA_BOT_PO_VISITOR_DATA}

FROM eclipse-temurin:17.0.12_7-jre-focal

COPY --from=builder /app/build/libs/CiabattaBot-all.jar /CiabattaBot-all.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /CiabattaBot-all.jar"]
