ECHO OFF

REM ###########
REM ECHO Maven setup if not already done.
REM ###########

REM SET MAVEN_PATH=C:/ApacheSF/apache-maven-3.5.0/bin
REM SET PATH=%MAVEN_PATH%;%PATH%

REM ###########
ECHO Build Package and Docker Container Deploy 
ECHO Build package (.war)
REM ###########

ECHO Copying ("overwriting to enforce") the handler engagement policy.
copy src\main\resources\handler-engagement-policies\policy-config-integrity-only.properties src\main\resources\policy-config.properties

ECHO Message Integrity: Fields, Pipe (SOAP Header) policy configuration defaults. 
copy src\main\resources\policy-defaults\message-integrity\integrity-validator-defaults-message-fields-pipe-soap-header.properties  src\main\resources\policy-defaults\integrity-validator-defaults.properties

CALL mvn clean package

ECHO We need to be in the Maven project that we are building. Paths will be resolved relatively.
SET PACKAGE_NAME=ixc-ecollection-profunds-api-v3.6-java-soap-webservice-2.0.0-configrity.war
SET PACKAGE_SOURCE=./target

REM ###########
REM ECHO Local Tomcat Deployment
REM ###########

REM SET PACKAGE_TARGET=C:/ApacheSF/apache-tomcat-9.0.34/webapps
REM copy %PACKAGE_SOURCE%/%PACKAGE_NAME %PACKAGE_TARGET%                                                                            

REM ###########
ECHO Local Docker Deployment
REM ###########

SET DEPLOYMENT_IMAGE_NAME=gaukamat/ixc-ecollection-profunds-api-v3.6-java-soap-webservice-acme
SET DEPLOYMENT_IMAGE_TAG=2.0.0-configrity-integrity-fields-pipe-soap-header
SET DEPLOYMENT_CONTAINER_NAME=ecol_configrity_kumonosu
SET PUBLISHED_API_PORT=9936

REM #Check what containers are running.
docker container ls    

ECHO Remove the docker container and the image. Housekeeping !
ECHO If the tag name or version changes, the older versions of image and contain will need to be removed manually.

docker container rm -f %DEPLOYMENT_CONTAINER_NAME%
docker image rm -f %DEPLOYMENT_IMAGE_NAME%:%DEPLOYMENT_IMAGE_TAG%

ECHO Build a new image and container from the new package.

docker build --rm  --tag %DEPLOYMENT_IMAGE_NAME%:%DEPLOYMENT_IMAGE_TAG% .
docker create --name %DEPLOYMENT_CONTAINER_NAME% --publish %PUBLISHED_API_PORT%:8080 %DEPLOYMENT_IMAGE_NAME%:%DEPLOYMENT_IMAGE_TAG%

REM #Start the container.
docker container start %DEPLOYMENT_CONTAINER_NAME%

REM #Check if the container is running.
docker container ls    

REM #Tail container log. 
docker logs --follow --timestamps %DEPLOYMENT_CONTAINER_NAME%

REM #Test using a tool (curl, JMeter) of your choice.


REM #[EOF]