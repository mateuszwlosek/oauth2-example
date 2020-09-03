FROM maven:3.6.3-jdk-11

WORKDIR /app

ADD . /app
RUN mvn clean package -DskipTests=true -Ddir=app
RUN mv target/oauth2-resource-server-1.0.0.jar oauth2-resource-server-1.0.0.jar

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Djava.security.egd=file:/dev/.urandom -jar oauth2-resource-server-1.0.0.jar"]
