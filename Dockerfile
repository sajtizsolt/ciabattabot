FROM eclipse-temurin:25-jdk AS builder

ENV GRADLE_USER_HOME="/app/.gradle"
ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx2g"

WORKDIR /app

COPY . .

RUN ./gradlew clean shadowJar --no-daemon \
    -Pkotlin.compiler.execution.strategy=in-process

FROM eclipse-temurin:25-jre

COPY --from=builder /app/build/libs/CiabattaBot-all.jar /CiabattaBot-all.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /CiabattaBot-all.jar"]
