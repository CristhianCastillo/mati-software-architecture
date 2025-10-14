FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp

RUN apk add curl

RUN apk add curl
RUN addgroup -S appgroup && adduser -S -G appgroup appuser && chown -R appuser:appgroup /tmp

COPY applications/app-service/build/libs/product-secondary-node-engine-ms.jar product-secondary-node-engine-ms.jar
RUN sh -c 'touch /product-secondary-node-engine-ms.jar'

ENV TZ=America/Bogota
ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar product-secondary-node-engine-ms.jar" ]