FROM ubuntu:latest

RUN apt-get -y update
RUN apt-get install -y openjdk-17-jre curl

ADD /target/codle-*-SNAPSHOT.jar /srv/codle.jar
ADD /src/main/java/Database/datastore.db /srv/main/java/Database/datastore.db

WORKDIR /srv
EXPOSE 5000
CMD ["java", "-jar", "codle.jar"]

