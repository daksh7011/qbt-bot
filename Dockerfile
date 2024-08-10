# escape=\
# syntax=docker/dockerfile:1

FROM openjdk:21-jdk-slim

RUN mkdir -p /bot/plugins
RUN mkdir -p /bot/data

RUN PWD
RUN ls -R

WORKDIR /builder

RUN PWD
RUN ls-R

COPY [ "build/libs/qbt-bot-*-all.jar", "/bot/bot.jar" ]

WORKDIR /bot

ENTRYPOINT [ "java", "-Xms2G", "-Xmx2G", "-jar", "/bot/bot.jar" ]
