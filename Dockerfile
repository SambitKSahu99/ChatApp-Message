FROM openjdk:17-jdk-slim
ADD build/libs/ChatApp-Message-0.0.1-SNAPSHOT.jar ChatApp-Message-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ChatApp-Message-0.0.1-SNAPSHOT.jar"]
EXPOSE 8083
