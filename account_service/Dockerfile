FROM openjdk:17-jdk

WORKDIR /app

COPY target/account_service-0.0.1-SNAPSHOT.jar /app/account_service-0.0.1-SNAPSHOT.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar","/app/account_service-0.0.1-SNAPSHOT.jar"]