FROM frolvlad/alpine-oraclejdk8:cleaned
maintainer Arpinum <equipe@arpinum.fr>

# install bash gcc openssl
RUN apk add --update bash gcc openssl &&  rm /var/cache/apk/*

# expose port
EXPOSE 8080

# Add sources
ENV destination /usr/src
COPY . ${destination}
WORKDIR ${destination}

#BUILD
RUN ./gradlew stage


# RUN
CMD ["bash", "run.sh"]
