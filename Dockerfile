FROM java:8
MAINTAINER youcyousyunn<731781984@qq.com>

COPY community-3.1.0-SNAPSHOT.jar /app/www/app.jar
CMD ["--server.port=8088"]

EXPOSE 8088

ENTRYPOINT ["nohup", "java", "-jar", "/app/www/app.jar", "&"]

