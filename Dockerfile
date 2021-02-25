# build with
#docker build -t imagename:TAG .

FROM markhobson/maven-chrome:jdk-11

ADD ./results /results
RUN chmod -R ugo+w /results
RUN mkdir /logs
RUN chmod -R ugo+w /logs
RUN mkdir /efs

ADD ./extentreports /extentreports
RUN chmod -R ugo+w /extentreports

ADD ./config.xml config.xml
RUN chmod -R ugo+w config.xml

ADD ./Datentreiber.xlsx Datentreiber.xlsx
RUN chmod ugo+w Datentreiber.xlsx


# Add App
#ADD ./target/SeleniumTestobject-1.0-SNAPSHOT.war app.war
EXPOSE 8080
RUN chmod -R u+w /efs
RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get install -y sqlite3 libsqlite3-dev
RUN /usr/bin/sqlite3 /results/test.db
ADD ./target/SeleniumTestobject-1.0-SNAPSHOT.war app.war
ENTRYPOINT ["java","-jar","app.war"]
