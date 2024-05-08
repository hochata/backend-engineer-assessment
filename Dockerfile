FROM eclipse-temurin:21 as build

WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY src src

RUN ./gradlew assemble
RUN mkdir build/dependency && (cd build/dependency; jar -xvf ../libs/*.jar)

FROM eclipse-temurin:21
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=build /workspace/app/src/main/resources/application.properties /config/application.properties
ENV SPRING_CONFIG_LOCATION /config/application.properties
ENTRYPOINT ["java","-cp","app:app/lib/*","com.midas.app.MidasApplication"]
