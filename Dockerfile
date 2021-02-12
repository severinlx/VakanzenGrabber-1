# build with
#docker build -t chrome:2.0 -f Dockerfile.dev .

FROM openjdk:11
RUN mkdir -p /logs
RUN mkdir -p /results

# Install Chrome
RUN apt-get update && apt-get install -y \
	apt-transport-https \
	ca-certificates \
	curl \
	gnupg \
	hicolor-icon-theme \
	libcanberra-gtk* \
	libgl1-mesa-dri \
	libgl1-mesa-glx \
	libpangox-1.0-0 \
	libpulse0 \
	libv4l-0 \
	fonts-symbola \
	--no-install-recommends \
	&& curl -sSL https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
	&& echo "deb [arch=amd64] https://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google.list \
	&& apt-get update && apt-get install -y \
	google-chrome-stable \
	--no-install-recommends \
	&& apt-get purge --auto-remove -y curl \
	&& rm -rf /var/lib/apt/lists/*

# Add chrome user
RUN groupadd -r chrome && useradd -r -g chrome -G audio,video chrome \
    && mkdir -p /home/chrome/Downloads && chown -R chrome:chrome /home/chrome
RUN apt-get update && apt-get install -y nano

RUN chmod ugo+w /var
RUN chmod ugo+w /logs
ADD ./extentreports /extentreports
RUN chmod -R ugo+w /extentreports

ADD ./config.xml config.xml
RUN chmod -R ugo+w config.xml

ADD ./Datentreiber.xlsx Datentreiber.xlsx
RUN chmod ugo+w Datentreiber.xlsx

# Run Chrome as non privileged user
USER chrome

# Add App
ADD /home/runner/work/VakanzenGrabber-1/VakanzenGrabber-1/target/SeleniumTestobject-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080 80
ENTRYPOINT ["java","-jar","app.war"]