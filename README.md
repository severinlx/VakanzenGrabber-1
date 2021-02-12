# VakanzenGrabber
## Beschreibung
Eine Webcrawler um neue freelancer jobs rauszufinden.
Gut als Training um Java/Selenium Framework aufzubauen.
Un für den Training für CI/CD Pipeline mit Docker/AWS ECS Fargate

## RUN
### a) um das Projeckt zu packagen:
mvn clean package spring-boot:repackage -DskipTests --file pom.xml
then run docker with:
docker build -t chrome:2.0 -f Dockerfile.dev .
docker run -p 8080:8080 chrome:

or run 
java -jar ./target/SeleniumTestobject-1.0-SNAPSHOT.war

### b) run directly from maven:
mvn spring-boot:run


## Check API
curl -i http://localhost:8080/get/results

