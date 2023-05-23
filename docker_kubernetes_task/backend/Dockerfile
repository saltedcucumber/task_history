FROM maven:3.8.6-eclipse-temurin-11-alpine AS builder 

WORKDIR /app

COPY . /app 

RUN mvn clean 
RUN mvn package

FROM openjdk:11

WORKDIR /app

COPY --from=builder /app/target/studentsystem-0.0.1-SNAPSHOT.jar /app

CMD ["java", "-jar", "/app/studentsystem-0.0.1-SNAPSHOT.jar"]


#var/lib/docker/overlay2/17064ae3f53921e696bf34385fbcecc9130d246527d0ddde98d7ed06b1932aeb/diff/app/target



#/app/target/studentsystem-0.0.1-SNAPSHOT.jar
