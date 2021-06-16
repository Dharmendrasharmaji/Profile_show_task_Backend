FROM openjdk:11-jre-slim
WORKDIR /usr/share/app
COPY ./target/useraccount-0.0.1-SNAPSHOT.jar myapp.jar
EXPOSE 8082
ENV MYSQL_DB=user_account_db
#ENV JWT_SECRET=secretkey
ENTRYPOINT ["java","-jar", "myapp.jar"]
