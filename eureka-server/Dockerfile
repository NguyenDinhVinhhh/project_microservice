FROM openjdk:17-jdk

WORKDIR /app

COPY target/eureka-server-0.0.1-SNAPSHOT.jar /app/eureka-service-0.0.1-SNAPSHOT.jar


ENTRYPOINT ["java", "-jar","/app/eureka-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8761