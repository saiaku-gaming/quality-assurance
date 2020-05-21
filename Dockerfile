# frontend build stage
FROM node:12.16-alpine as frontend-build-stage
WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend .
RUN npm run build

# prepare maven
FROM maven:3-jdk-11 as backend-maven-build-stage
WORKDIR /build
COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins
RUN mvn dependency:go-offline

# backend build stage
FROM maven:3-jdk-11 as backend-build-stage
WORKDIR /build
COPY pom.xml .
COPY src /build/src
COPY --from=backend-maven-build-stage /root/.m2 /root/.m2
COPY --from=frontend-build-stage /app/dist /build/src/main/resources/static/
RUN mvn package

# final run step
FROM openjdk:11-jre-slim as runtime
EXPOSE 8080
ENV APP_HOME /app
RUN mkdir $APP_HOME
RUN mkdir $APP_HOME/log
RUN mkdir $APP_HOME/data
VOLUME $APP_HOME/log
VOLUME $APP_HOME/data
WORKDIR $APP_HOME
COPY --from=backend-build-stage /build/target/*.jar app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
