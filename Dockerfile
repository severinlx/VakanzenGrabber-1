FROM amazoncorretto:11
ADD /home/runner/work/VakanzenGrabber-1/VakanzenGrabber-1/target/SeleniumTestobject-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080 80
ENTRYPOINT ["java","-jar","app.jar"]
