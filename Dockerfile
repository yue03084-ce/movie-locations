# ―― 阶段一:构建。用带 Maven+JDK 的镜像把源码打成 jar ――
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# ―― 阶段二:运行。只带 JRE 的干净镜像,放入 jar ――
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]