FROM java:8-jre-alpine
maintainer Jean-Baptiste Dusseaut <equipe@arpinum.fr>

ENV destination /app
EXPOSE 5050
COPY build/libs/app.jar ${destination}/app.jar
WORKDIR ${destination}
CMD ["java", "-jar", "app.jar"]