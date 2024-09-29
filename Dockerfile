FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN apt-get update && apt-get install -y tzdata
ENV TZ=Asia/Kolkata
RUN ./gradlew build
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app/build/libs/ChatApp-Message-0.0.1-SNAPSHOT.jar"]
