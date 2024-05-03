FROM openjdk:17-jdk-alpine
MAINTAINER Petrign
COPY server.jar app.jar
COPY postgresql-42.7.3.jar psql.jar
COPY db.cfg db.cfg
COPY db.cfg db1.cfg
COPY script1.gl script1.gl
COPY script2.gl script2.gl
COPY script3.gl script3.gl
ENTRYPOINT ["java","-cp","psql.jar:app.jar", "ru.se.ifmo.prog.lab8.server.Main"]
