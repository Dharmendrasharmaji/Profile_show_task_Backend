FROM openjdk:11-jre-slim
WORKDIR /usr/share/app
COPY ./target/userRecomService-0.0.1-SNAPSHOT.jar myapp.jar
EXPOSE 8081
ENV MONGO_DB=userRecom_db
ENV JWT_SECRET=secretkey
ENTRYPOINT ["java","-jar", "myapp.jar"]