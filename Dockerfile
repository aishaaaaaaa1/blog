# Build
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests -B

# Run
FROM eclipse-temurin:21-jre-jammy
ENV CATALINA_HOME=/opt/tomcat
RUN apt-get update && apt-get install -y --no-install-recommends curl && \
    curl -sL https://dlcdn.apache.org/tomcat/tomcat-11/v11.0.0-M20/bin/apache-tomcat-11.0.0-M20.tar.gz | tar xz -C /opt && \
    mv /opt/apache-tomcat-11.0.0-M20 /opt/tomcat && \
    rm -rf /opt/tomcat/webapps/* && \
    apt-get purge -y curl && apt-get autoremove -y && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/blogg.war /opt/tomcat/webapps/ROOT.war

# Render injecte PORT ; par défaut 8080 en local
ENV PORT=8080
EXPOSE 8080
CMD sed -i "s/port=\"8080\"/port=\"${PORT}\"/" /opt/tomcat/conf/server.xml && /opt/tomcat/bin/catalina.sh run
