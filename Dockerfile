FROM maven:3.9.2-eclipse-temurin-17-alpine AS cleaninstall
VOLUME /tmp

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app/pom.xml

RUN mvn -f /usr/src/app/pom.xml clean install

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 443
MAINTAINER kobe2.com
COPY --from=cleaninstall usr/src/app/target/escritura-auth-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", \
"-Dspring.profiles.active=staging", \
"app.jar" ]
