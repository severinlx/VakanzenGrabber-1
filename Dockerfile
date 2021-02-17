# build with
#docker build -t imagename:TAG .

FROM markhobson/maven-chrome:jdk-11

ADD ./results /results
RUN chmod -R ugo+w /results
RUN mkdir /logs
RUN chmod -R ugo+w /logs

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