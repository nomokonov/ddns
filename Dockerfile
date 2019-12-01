FROM openjdk:8
ADD target/ddns-service.jar ddns-service.jar
EXPOSE 3636
ENTRYPOINT  ["java","-jar","ddns-service.jar"]
