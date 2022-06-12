FROM java:8

EXPOSE 9090

ADD management-system-0.0.1-SNAPSHOT app.jar
RUN bash -c 'touch /app.jsr'

ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=pro"]