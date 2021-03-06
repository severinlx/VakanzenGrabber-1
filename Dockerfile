# build with
#docker build -t imagename:TAG .

FROM markhobson/maven-chrome:jdk-11
RUN mkdir /logs
RUN chmod -R ugo+w /logs
RUN mkdir /efs
RUN chmod -R u+w /efs

ADD ./extentreports /extentreports
RUN chmod -R ugo+w /extentreports

ADD ./config.xml config.xml
RUN chmod -R ugo+w config.xml

ADD ./Datentreiber.xlsx Datentreiber.xlsx
RUN chmod ugo+w Datentreiber.xlsx

# Add App
ADD ./target/SeleniumTestobject-1.0-SNAPSHOT.war app.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.war"]

