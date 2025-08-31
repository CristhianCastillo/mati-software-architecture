FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp

RUN apk add curl
RUN addgroup -S appgroup && adduser -S -G appgroup appuser && chown -R appuser:appgroup /tmp

COPY applications/app-service/build/libs/matching-engine-ms.jar matching-engine-ms.jar
RUN sh -c 'touch /matching-engine-ms.jar'

ENV TZ=America/Bogota
ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar matching-engine-ms.jar" ]