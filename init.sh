#!/bin/bash

# Gradle을 사용하여 프로젝트 빌드
./gradlew clean build -x test

# Docker 이미지 빌드
sudo docker build -t helloaway/petwalk:1.0 .

# Docker Hub에 이미지 푸시
docker push helloaway/petwalk:1.0