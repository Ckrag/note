FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
RUN apt-get update
RUN apt-get -qq -y install \
    curl \
    unzip \
    zip
RUN curl -s https://get.sdkman.io | bash
RUN /bin/bash -c "source /root/.sdkman/bin/sdkman-init.sh; sdk version; sdk install micronaut 3.4.0"
COPY . /backend
WORKDIR backend
RUN ./gradlew build
EXPOSE 8080
CMD ./gradlew run