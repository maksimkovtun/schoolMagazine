FROM eclipse-temurin:17-jdk AS build
WORKDIR /application
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /application
COPY --from=build /application/target/schoolMagazine-0.0.1-SNAPSHOT.jar schoolMagazine.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "schoolMagazine.jar"]
