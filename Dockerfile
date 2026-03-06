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
ENV PORT=7860

RUN useradd -m -u 1000 user && \
    apt-get update && apt-get install -y --no-install-recommends curl && \
    curl -sL https://dlcdn.apache.org/tomcat/tomcat-11/v11.0.18/bin/apache-tomcat-11.0.18.tar.gz | tar xz -C /opt && \
    mv /opt/apache-tomcat-11.0.18 /opt/tomcat && \
    rm -rf /opt/tomcat/webapps/* && \
    chown -R user:user /opt/tomcat && \
    apt-get purge -y curl && apt-get autoremove -y && rm -rf /var/lib/apt/lists/*

COPY --from=build --chown=user /app/target/blogg.war /opt/tomcat/webapps/ROOT.war

USER user
EXPOSE 7860

CMD sed -i "s/port=\"8080\"/port=\"${PORT}\"/" /opt/tomcat/conf/server.xml && /opt/tomcat/bin/catalina.sh run
