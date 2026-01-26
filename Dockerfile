FROM amazonlinux:2023

RUN yum install -y java-21-amazon-corretto \
    && yum clean all

WORKDIR /app

COPY target/perspectiabackend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]