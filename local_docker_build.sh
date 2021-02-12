#!/bin/bash
#chmod +x ~/local_docker_build.sh
TAG="1.2.1"
mvn clean package spring-boot:repackage -DskipTests --file pom.xml
docker build -t chrome:TAG -f Dockerfile.dev .
docker run -p 8080:8080 chrome:TAG




