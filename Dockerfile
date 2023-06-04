FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8080
MAINTAINER kobe2.com
COPY escritura-springboot-auth/target/escritura-auth-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", \
"-Dspring.profiles.active=dev", \
"-Dcustom.mq.topic=email", \
"-Dcustom.mq.virtualHost=virtualHost", \
"-Dcustom.mq.host=host", \
"-Dcustom.mq.username=username", \
"-Dcustom.mq.password=password", \
"-Dcustom.mq.queue=queue", \
"-Dcustom.platform.identifier.code=code", \
"-Dcustom.pw.encryption.code=code", \
"-jar", \
"app.jar" ]
